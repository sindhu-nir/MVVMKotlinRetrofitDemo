package com.btracsolutions.yesparking.utils.permission



import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.btracsolutions.yesparking.R
import java.lang.ref.WeakReference

class PermissionManager private constructor(private val lifecycleOwner : WeakReference<LifecycleOwner>) : DefaultLifecycleObserver{

    private val requiredPermissions = mutableListOf<Permissions>()
    private var rationaleDescription: String? = null
    private var rationaleTitle: String? = null
    private var permanentlyDeniedDescription: String? = null
    private var callback: (Boolean) -> Unit = {}
    private var intent : Intent? = null
    private var detailedCallback: (Map<String, Boolean>) -> Unit = {}
    private val deniedList = arrayListOf<String>()
    private lateinit var permissionCheck : ActivityResultLauncher<Array<String>>
    private var activity : AppCompatActivity? = null

    init {
        lifecycleOwner.get()?.lifecycle?.addObserver(this)
    }
    override fun onCreate(owner: LifecycleOwner) {
        permissionCheck = if (owner is AppCompatActivity) {
            owner.registerForActivityResult(RequestMultiplePermissions()) { grantResults ->
                sendResultAndCleanUp(grantResults)
            }
        } else {
            (owner as Fragment).registerForActivityResult(RequestMultiplePermissions()) { grantResults ->
                sendResultAndCleanUp(grantResults)
            }
        }
        activity = if(lifecycleOwner.get() is Fragment) (lifecycleOwner.get() as? Fragment)?.context?.scanForActivity() else lifecycleOwner.get() as? AppCompatActivity
        super.onCreate(owner)
    }

    companion object {
        fun from(lifecycleOwner: LifecycleOwner) = PermissionManager(WeakReference(lifecycleOwner))

        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
        fun sdkEqOrAbove33() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
        fun sdkEqOrAbove29() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
        fun sdkEqOrAbove30() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
        fun sdkEqOrAbove31() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
        fun sdkEqOrAbove28() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

        @SuppressLint("ObsoleteSdkInt")
        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.M)
        fun sdkEqOrAbove23() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    fun rationale(description: String, title : String = activity?.getString(R.string.permission_title) ?: ""): PermissionManager {
        rationaleDescription = description
        rationaleTitle = title
        return this
    }

    fun request(vararg permission: Permissions): PermissionManager {
        requiredPermissions.addAll(permission)
        return this
    }

    fun permissionPermanentlyDeniedIntent(intent: Intent): PermissionManager {
        this.intent = intent
        return this
    }

    fun permissionPermanentlyDeniedContent(description : String = ""): PermissionManager {
        this.permanentlyDeniedDescription = description.ifEmpty { activity?.getString(R.string.permission_description) }
        return this
    }

    fun checkAndRequestPermission(callback: (Boolean) -> Unit) {
        this.callback = callback
        handlePermissionRequest()
    }

    fun checkAndRequestDetailedPermission(callback: (Map<String, Boolean>) -> Unit) {
        this.detailedCallback = callback
        handlePermissionRequest()
    }

    private fun handlePermissionRequest() {
        // 1 TRUE -> When user has denied the permission at-least once -> rationale alert-dialog show
        // 2 FALSE -> i. User has never requested the permission -> Show permission pop-up
        //            ii. User has denied the permission permanently -> Settings
        activity?.let { activity ->
            if (areAllPermissionsGranted(activity)) {
                sendPositiveResult()
            } else if (shouldShowPermissionRationale(activity)) {
                getPermissionList().forEach {
                    PermissionsPreferences.updatePermissionStatus(it, true)
                }
                val requiresRationaleList = getPermissionList().map { Pair(it,requiresRationale(activity,it)) }
                displayRationale(activity, getCommaSeparatedFormattedString(requiresRationaleList.filter {it.second}.map {it.first}))
            } else {
                if (getPermissionList().any{!PermissionsPreferences.getPermissionStatus(it) }) {
                    requestPermissions()
                } else {
                    displayPermanentlyDenied(activity,getCommaSeparatedFormattedString(
                        getPermissionList().filter {isPermanentlyDenied(activity,it)}))
                    cleanUp()
                }
            }
        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun displayRationale(activity: AppCompatActivity, permission: String?) {
        AlertDialog.Builder(activity)
            .setTitle(rationaleTitle ?: activity.getString(R.string.permission_title))
            .setMessage(rationaleDescription ?: activity.getString(R.string.permission_description, permission ?: ""))
           // .setMessage("TEST")
            .setCancelable(true)
            .setNegativeButton(activity.getString(R.string.cancel)){dialog,_ ->
                dialog.dismiss()
                cleanUp() }
            .setPositiveButton(activity.getString(R.string.ok)){ _, _ -> requestPermissions()}
            .show()
    }


    @SuppressLint("StringFormatInvalid")
    private fun displayPermanentlyDenied(activity: AppCompatActivity, permission: String?) {
        AlertDialog.Builder(activity)
            .setTitle(activity.getString(R.string.permission_title))
            .setMessage(permanentlyDeniedDescription ?: activity.getString(R.string.permission_description_permanently, permission ?: ""))
           // .setMessage("Permanently deny")
            .setCancelable(true)
            .setNegativeButton(activity.getString(R.string.cancel)){dialog,_->
                dialog.dismiss()
                cleanUp()}
            .setPositiveButton(activity.getString(R.string.go_to_settings)) { _, _ ->
                val finalIntent = if(intent != null) {
                    intent
                }else{
                    val intent2 = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + activity.packageName)
                    )
                    intent2.addCategory(Intent.CATEGORY_DEFAULT)
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent2
                }
                activity.startActivity(finalIntent)
            }.show()
    }

    private fun sendPositiveResult() {
        sendResultAndCleanUp(getPermissionList().associateWith { true })
    }

    private fun sendResultAndCleanUp(grantResults: Map<String, Boolean>) {
        if(deniedList.isNotEmpty()){
            activity?.let { displayPermanentlyDenied(it, getCommaSeparatedFormattedString(deniedList)) }
        }else{
            callback(grantResults.all { it.value })
            detailedCallback(grantResults)
        }
        cleanUp()
    }

    private fun cleanUp() {
        requiredPermissions.clear()
        rationaleDescription = null
        permanentlyDeniedDescription = null
        deniedList.clear()
        callback = {}
        detailedCallback = {}
    }
    // 4 -> 2 NEW 2 Permanently denied
    private fun requestPermissions() {
        val list = getPermissionList()
        val deniedList = list.filter {isPermanentlyDenied(activity,it)}
        this.deniedList.addAll(deniedList)
        val finalList = list.subtract(deniedList.toSet())
        permissionCheck.launch(finalList.toTypedArray())
    }

    private fun areAllPermissionsGranted(activity: AppCompatActivity) =
        requiredPermissions.all { it.isGranted(activity) }

    private fun shouldShowPermissionRationale(activity: AppCompatActivity) =
        requiredPermissions.any { it.requiresRationale(activity) }

    private fun getPermissionList() =
        requiredPermissions.flatMap { it.permissions.toList() }.toTypedArray()

    private fun Permissions.isGranted(activity: AppCompatActivity) =
        permissions.all { hasPermission(activity, it) }

    private fun Permissions.requiresRationale(activity: AppCompatActivity): Boolean {
        return   permissions.any { activity.shouldShowRequestPermissionRationale(it) }
    }

    private fun requiresRationale(activity: AppCompatActivity?, permission: String) =
        activity?.shouldShowRequestPermissionRationale(permission) ?: false
    private fun isPermanentlyDenied(activity: AppCompatActivity?, permission: String) =
        !requiresRationale(activity,permission) && PermissionsPreferences.getPermissionStatus(permission)
    private fun hasPermission(activity: AppCompatActivity, permission: String) =
        ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    private fun getCommaSeparatedFormattedString(permissions : List<String>) : String?{
        val newList = mapPermissionsToStrings(permissions)
        val list = newList.toMutableList()
        return if(list.size == 1){
            list.first()
        }else {
            list.removeLast()
            val string = list.joinToString(", ")
            string + " and " + newList.last()
        }
    }

    private fun mapPermissionsToStrings(list: List<String>) : List<String?>{
        return list.map {
            when(it){
                Manifest.permission.POST_NOTIFICATIONS -> activity?.getString(R.string.post_notifications)
                Manifest.permission.WAKE_LOCK -> activity?.getString(R.string.wake_lock)
                Manifest.permission.INTERNET -> activity?.getString(R.string.internet)
                Manifest.permission.ACCESS_NETWORK_STATE -> activity?.getString(R.string.access_network_state)
//                Manifest.permission.READ_CALENDAR -> activity?.getString(R.string.read_calendar)
//                Manifest.permission.WRITE_CALENDAR -> activity?.getString(R.string.write_calendar)
                Manifest.permission.READ_EXTERNAL_STORAGE -> activity?.getString(R.string.read_external_storage)
                Manifest.permission.READ_MEDIA_IMAGES -> activity?.getString(R.string.read_media_images)
//                Manifest.permission.READ_MEDIA_VIDEO -> activity?.getString(R.string.read_media_video)
//                Manifest.permission.READ_MEDIA_AUDIO -> activity?.getString(R.string.read_media_audio)
                Manifest.permission.SCHEDULE_EXACT_ALARM -> activity?.getString(R.string.schedule_exact_alarm)
                Manifest.permission.WRITE_EXTERNAL_STORAGE -> activity?.getString(R.string.write_external_storage)
                Manifest.permission.CAMERA -> activity?.getString(R.string.camera)
//                Manifest.permission.READ_PHONE_STATE -> activity?.getString(R.string.read_phone_state)
//                Manifest.permission.READ_PHONE_NUMBERS -> activity?.getString(R.string.read_phone_numbers)
//                Manifest.permission.GET_ACCOUNTS -> activity?.getString(R.string.get_accounts)
          //      Manifest.permission.FOREGROUND_SERVICE -> activity?.getString(R.string.foreground_service)
                Manifest.permission.ACCESS_FINE_LOCATION -> activity?.getString(R.string.access_fine_location)
                Manifest.permission.ACCESS_COARSE_LOCATION -> activity?.getString(R.string.access_coarse_location)
//                Manifest.permission.RECEIVE_BOOT_COMPLETED -> activity?.getString(R.string.receive_boot_completed)
//                Manifest.permission.READ_CONTACTS -> activity?.getString(R.string.read_contacts)
//                Manifest.permission.RECORD_AUDIO -> activity?.getString(R.string.record_audio)
                Manifest.permission.ACCESS_WIFI_STATE -> activity?.getString(R.string.access_wifi_state)
//                Manifest.permission.MODIFY_AUDIO_SETTINGS -> activity?.getString(R.string.modify_audio_settings)
//                Manifest.permission.BLUETOOTH -> activity?.getString(R.string.bluetooth)
//                Manifest.permission.BLUETOOTH_CONNECT -> activity?.getString(R.string.bluetooth_connect)
//                Manifest.permission.ACTIVITY_RECOGNITION -> activity?.getString(R.string.activity_recognition)
//                Manifest.permission.USE_FULL_SCREEN_INTENT -> activity?.getString(R.string.use_full_screen_intent)
//                Manifest.permission.VIBRATE -> activity?.getString(R.string.vibrate)
                else -> "Other"
            }
        }
    }
}

fun Context.scanForActivity(): AppCompatActivity? {
    return when (this) {
        is AppCompatActivity -> this
        is ContextWrapper -> baseContext.scanForActivity()
        else -> {
            null
        }
    }
}


//import android.app.Activity
//import android.content.pm.PackageManager
//import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import com.btracsolutions.yesparking.R
//import com.btracsolutions.yesparking.dailog.CommonInfoDialogFragment
//import java.lang.ref.WeakReference

//class PermissionManager private constructor(private val fragment: WeakReference<Activity>) {
//
//    private val requiredPermissions = mutableListOf<Permission>()
//    private var rationale: String? = null
//    private var callback: (Boolean) -> Unit = {}
//    private var detailedCallback: (Map<Permission,Boolean>) -> Unit = {}
//
//    private val permissionCheck =
//        fragment.registerForActivityResult(RequestMultiplePermissions()) { grantResults ->
//            sendResultAndCleanUp(grantResults)
//        }
//
//    companion object {
//        fun from(fragment: Activity) = PermissionManager(WeakReference(fragment))
//    }
//
//    fun rationale(description: String): PermissionManager {
//        rationale = description
//        return this
//    }
//
//    fun request(vararg permission: Permission): PermissionManager {
//        requiredPermissions.addAll(permission)
//        return this
//    }
//
//    fun checkPermission(callback: (Boolean) -> Unit) {
//        this.callback = callback
//        handlePermissionRequest()
//    }
//
//    fun checkDetailedPermission(callback: (Map<Permission,Boolean>) -> Unit) {
//        this.detailedCallback = callback
//        handlePermissionRequest()
//    }
//
////    private fun handlePermissionRequest() {
////        fragment.get()?.let { fragment ->
////            when {
////                areAllPermissionsGranted(fragment) -> sendPositiveResult()
////                shouldShowPermissionRationale(fragment) -> displayRationale(fragment)
////                else -> requestPermissions()
////            }
////        }
////    }
//private fun handlePermissionRequest() {
//    fragment.let { fragment ->
//        when {
//            areAllPermissionsGranted(fragment) -> sendPositiveResult()
//            shouldShowPermissionRationale(fragment) -> displayRationale(fragment)
//            else -> requestPermissions()
//        }
//    }
//}
//
//    private fun displayRationale(fragment: WeakReference<Activity>) {
////        val customDialog = CustomDialog.getInstance(fragment.requireContext())
////            .setTitle(fragment.getString(R.string.dialog_permission_title))
////            .setMessage(rationale ?: fragment.getString(R.string.dialog_permission_default_message))
////            .setIcon(ContextCompat.getDrawable(fragment.requireContext(), R.drawable.ic_logo))
////            .setPositiveButton(
////                fragment.getString(R.string.dialog_permission_button_positive),
////                object : CustomDialog.ICustomDialogClickListener {
////                    override fun onClick() {
////                        requestPermissions()
////                    }
////                })
////            .createDialog()
////        customDialog.showDialog()
//        fragment.get()?.let {
//            showCommonTutorial(
//                it.getString(R.string.opps),
//                it.getString(R.string.select_bike_type),
//                R.drawable.ic_parking_unavailable_bike,it
//            )
//        }
//    }

//    }
//
//    private fun sendPositiveResult() {
//        sendResultAndCleanUp(getPermissionList().associate { it to true } )
//    }
//
//    private fun sendResultAndCleanUp(grantResults: Map<String, Boolean>) {
//        callback(grantResults.all { it.value })
//        detailedCallback(grantResults.mapKeys { Permission.from(it.key) })
//        cleanUp()
//    }
//
//    private fun cleanUp() {
//        requiredPermissions.clear()
//        rationale = null
//        callback = {}
//        detailedCallback = {}
//    }
//
//    private fun requestPermissions() {
//        permissionCheck?.launch(getPermissionList())
//    }
//
//    private fun areAllPermissionsGranted(fragment: WeakReference<Activity>) =
//        requiredPermissions.all { it.isGranted(fragment) }
//
//    private fun shouldShowPermissionRationale(fragment: WeakReference<Activity>) =
//        requiredPermissions.any { it.requiresRationale(fragment) }
//
//    private fun getPermissionList() =
//        requiredPermissions.flatMap { it.permissions.toList() }.toTypedArray()
//
//    private fun Permission.isGranted(fragment: WeakReference<Activity>) =
//        permissions.all { hasPermission(fragment, it) }
//
//    private fun Permission.requiresRationale(fragment: Fragment) =
//        permissions.any { fragment.shouldShowRequestPermissionRationale(it) }
//
//    private fun hasPermission(fragment: WeakReference<Activity>, permission: String) =
//        fragment.get()?.let {
//            ContextCompat.checkSelfPermission(
//                it,
//                permission
//            )
//        } == PackageManager.PERMISSION_GRANTED
//}

