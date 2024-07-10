package com.btracsolutions.yesparking.activity.auth.login

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.btracsolutions.yesparking.activity.BaseActivity
import com.btracsolutions.yesparking.activity.WelcomeActivity
import com.btracsolutions.yesparking.activity.auth.AuthViewModel
import com.btracsolutions.yesparking.activity.auth.forgotpass.ForgotPassActivity
import com.btracsolutions.yesparking.activity.auth.signup.RegistrationActivity
import com.btracsolutions.yesparking.activity.mandatoryentries.MandatoryEntriesActivity

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity(), View.OnClickListener,
    LanguageBottomFragment.BottomSheetLanguageClickListener {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var viewModel: AuthViewModel
    var fcmid = ""
    var isEyeOpen = false
//    val permissionManager = PermissionManager.from(this@LoginActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
        // statusBarColor(R.color.white)


        initView()
        initUserCredentials()
        initListeners()
        getFCMToken()
        initViewModel()
        LoadLang()

    }

    //    private fun checkDetailedPermissionsAndAccessFeature() {
//        val intentWhenDeniedPermanently = Intent()
//        permissionManager
//            .request(Permissions.Location)
//            .rationale(description = "Please approve permission to access this feature", title ="Permission required")
//            .permissionPermanentlyDeniedIntent(intentWhenDeniedPermanently)
//            .permissionPermanentlyDeniedContent(description= "To access this feature we need permission please provide access to app from app settings")
//            .checkAndRequestDetailedPermission {
//                showToast(it.toString())
//            }
//    }
//    private fun checkPermissionsAndAccessFeature() {
//        val intentWhenDeniedPermanently = Intent()
//        permissionManager
//            .request(Permissions.Location)
//            .rationale(description = "Please approve permission to access this feature", title ="Permission required")
//            .permissionPermanentlyDeniedIntent(intentWhenDeniedPermanently)
//            .permissionPermanentlyDeniedContent(description= "To access this feature we need permission please provide access to app from app settings")
//            .checkAndRequestPermission {
//                if(it) showToast("Permisssion granted successfully")
//                else
//                    showToast(getString(R.string.need_this_permission_msg))
//            }
//    }
    private fun initUserCredentials() {
        try {
            val userCredential = Gson().fromJson(MySharedPref.getString(PrefKey.CREDENTIALS),
                object : TypeToken<UserCredential?>() {}.type) as UserCredential
            if (!userCredential.phone.isNullOrBlank()) {
                binding.etPhone.setText(userCredential.phone)
                binding.etPass.setText(userCredential.password)
                binding.cbRemember.isChecked = true
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        viewModel.getSignInResponse().observe(this, {
            consumeResponse(it)
        })

    }

    private fun initView() {
        var termsAndConditions =
            "<html>By login, you agree to Yes Parkings's <a href=\"https://yesparkingbd.com/terms-and-conditions.html\">Terms and Conditions</a> and acknowledge that you have read our <a href=\"https://yesparkingbd.com/policy.html\">Privacy Policy</a>.</html>"

        if (LocaleHelper.getLanguage(this).contentEquals(AppConstants.LAN_BN)) {
            termsAndConditions =
                "<html>লগইন করে, আপনি ইয়েস পার্কিং এর <a href=\"https://yesparkingbd.com.com/terms-and-conditions.html\">নিয়ম ও শর্তাবলী</a>সাথে একমত এবং স্বীকার করছেন যে আপনি আমাদের <a href=\"https://yesparkingbd.com/policy.html\">গোপনীয়তা নীতি</a>পড়েছেন।</html>"

        }



        binding.tvTerms.text =
            HtmlCompat.fromHtml(termsAndConditions, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.tvTerms.movementMethod = LinkMovementMethod.getInstance()
        binding.toolbar.tvTitle.text = getString(R.string.login)
        binding.tvForgotPass.text = getString(R.string.forgot_pass) + "?"
        binding.toolbar.llBack.visibility = View.VISIBLE
       // binding.toolbar.ivFlag.visibility = View.VISIBLE
        binding.toolbar.ivFlag.setImageResource(R.drawable.ic_usa)
//        val flagEmoji = "\uD83C\uDDFA\uD83C\uDDF8" // Unicode for US flag
//        val flagEmoji = "\uD83C\uDDE7\uD83C\uDDE9" // Unicode for Bangladesh flag
//        val bitmap = createEmojiBitmap(flagEmoji)
//        binding.toolbar.ivFlag.setImageBitmap(bitmap)
    }

    private fun initListeners() {
        binding?.btNext?.setOnClickListener(this)
        binding?.tvSignup?.setOnClickListener(this)
        binding?.ivEye?.setOnClickListener(this)
        binding?.tvForgotPass?.setOnClickListener(this)
//        binding?.btSkip?.setOnClickListener(this)
        binding?.toolbar?.llBack?.setOnClickListener(this)
        binding?.toolbar?.ivFlag?.setOnClickListener(this)


    }

    private fun getFCMToken() {
        if (MySharedPref.getString(PrefKey.FCM_DEVICE_TOKEN).toString().isNullOrBlank()) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(
                        ContentValues.TAG,
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                fcmid = task.result
                println("FCM token " + fcmid)

                // Log and toast
                //val msg = getString(R.string.msg_token_fmt, token)
                // Log.d(TAG, msg)
            })
        } else {
            fcmid = MySharedPref.getString(PrefKey.FCM_DEVICE_TOKEN).toString()
            println("FCM token " + fcmid)
        }
    }

    private fun signInNow(mobile: String, password: String) {
        if (NetworkConnectivityChecker.isOnline()) {
            val hashMap = HashMap<String, String>()
            hashMap["countryCode"] = "+880"
            hashMap["mobileNumber"] = mobile
            hashMap["password"] = password
            hashMap["deviceType"] = "Android"
            hashMap["deviceKey"] = fcmid

            viewModel.sendLoginRequest(hashMap)
        } else {
            showToast(getString(R.string.internet_not_available))
        }
        //viewModel.sendLoginRequest(userName, password)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding?.btNext?.id -> {
                Utils.preventTwoClick(v!!)
                if (validateData()) {
                    var substractPhone = isPhoneStartsWithZero(binding.etPhone.text.toString())
                    signInNow(substractPhone, "${binding?.etPass?.text}")
                }
            }

            binding?.tvSignup?.id -> {

                val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
                // intent.putExtra("news",Gson().toJson(data).toString())
                startActivity(intent)
                finish()
            }

            binding?.tvForgotPass?.id -> {

                val intent = Intent(this@LoginActivity, ForgotPassActivity::class.java)
                startActivity(intent)
                finish()
            }

            binding?.toolbar?.llBack?.id -> {
                val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }

            binding?.toolbar?.ivFlag?.id -> {
                Utils.preventTwoClick(v!!)
                LanguageBottomFragment(
                    this,
                    LocaleHelper.getLanguage(this)
                ).apply {
                    show(supportFragmentManager, tag)
                }
            }

            binding?.ivEye?.id -> {
                if (isEyeOpen) {
                    isEyeOpen = false
                    binding.ivEye.setImageResource(R.drawable.ic_eye_close)
                    binding.etPass.inputType = InputType.TYPE_NUMBER_VARIATION_PASSWORD
                    binding.etPass.setTransformationMethod(PasswordTransformationMethod.getInstance())
                } else {
                    isEyeOpen = true
                    binding.ivEye.setImageResource(R.drawable.ic_eye_open)
                    binding.etPass.inputType = InputType.TYPE_CLASS_TEXT
                    binding.etPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance())

                }
            }
        }
    }

    private fun validateData(): Boolean {
        if (binding.etPhone.text.toString()
                .isNullOrBlank() || binding.etPhone.text.toString().length < 10 || binding.etPhone.text.toString().length > 11
        ) {
            showToast(getString(R.string.invalid_mobile))
            return false
        }
        if (binding.etPass.text.isNullOrBlank() || binding.etPass.text.toString().length < 6) {
            showToast(getString(R.string.msg_txt_enter_pass))
            return false
        }

        return true
    }

    private fun consumeResponse(apiResponse: ApiResponse) {
        Log.d("ApiTesting", "consumeResponse 121")
        when (apiResponse.status) {
            Status.LOADING -> {
                showProgressBar()
            }

            Status.SUCCESS -> {
                hideProgressBar()

                if (apiResponse.requestType!!.equals(ApiConstants.REQUEST_TYPE_LOGIN)) {
                    try {
                        val successResponse = apiResponse.data as LoginResponse
                        if (successResponse.success) {
                            MySharedPref.setBoolean(PrefKey.IS_LOGGEDIN, true)
                            MySharedPref.setString(
                                PrefKey.TOKEN,
                                successResponse.data.tokens.accessToken
                            )
                            MySharedPref.setString(
                                PrefKey.REFRESH_TOKEN,
                                successResponse.data.tokens.refreshToken
                            )
                            MySharedPref.setString(
                                PrefKey.USER_ID,
                                successResponse.data.userData._id
                            )
                            MySharedPref.setString(
                                PrefKey.USER,
                                Gson().toJson(successResponse.data)
                            )

                            if (binding.cbRemember.isChecked) {
                                setUserCredentials(true)
                            } else {
                                setUserCredentials(false)
                            }

                            if (successResponse.data.vehicles.isNullOrEmpty()) {
                                val intent =
                                    Intent(this@LoginActivity, MandatoryEntriesActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                                        or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                                finish()
                            } else {
                                val intent =
                                    Intent(this@LoginActivity, DashBoardActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                                        or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                                finish()
                            }


                        } else {
                            showToast(successResponse.message)
                        }
                    } catch (e: Exception) {
                        println(e.message)
                    }


                }
            }

            Status.FAILED -> {
                hideProgressBar()
                if (apiResponse.requestType!! == ApiConstants.REQUEST_TYPE_LOGIN) {
                    showToast(apiResponse.message)
                    // handleErrorResponse(commonResponse)
                }
            }

            Status.ERROR -> {
                hideProgressBar()
                apiResponse.message?.let {
                    notifyLongMessage(it)
                }
            }
        }
    }

    private fun hideProgressBar() {
        binding?.progressBar?.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    private fun isPhoneStartsWithZero(phone: String): String {
        var substractPhone = ""
        if (phone.startsWith("0")) {
            substractPhone = phone.substring(1, phone.length)
        } else {
            substractPhone = phone
        }
        return substractPhone
    }

    private fun setUserCredentials(isSet: Boolean) {
        val userCredential =
            UserCredential(binding.etPhone.text.toString(), binding.etPass.text.toString())
        MySharedPref.setString(PrefKey.CREDENTIALS, "")
        if (isSet) {
            MySharedPref.setString(PrefKey.CREDENTIALS, Gson().toJson(userCredential))
        }
    }

    override fun onResume() {
        //checkPermissionsAndAccessFeature()
        //  checkDetailedPermissionsAndAccessFeature()
        super.onResume()
    }

    override fun onLanguageSelection(isBangla: Boolean) {

      //  changeLang()
//        when (LocaleHelper.getLanguage(this)) {
//            AppConstants.LAN_EN -> changeLang()
//            AppConstants.LAN_BN -> changeLang()
//            else -> {
//
//            }
//        }

//
        if (isBangla) {
            if (LocaleHelper.getLanguage(this).contentEquals(AppConstants.LAN_EN)) {
                    changeLang()
            }
        }else{
            if (LocaleHelper.getLanguage(this).contentEquals(AppConstants.LAN_BN)) {
                changeLang()
            }
        }
    }

    private fun createEmojiBitmap(emoji: String): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = 100f
        paint.color = ContextCompat.getColor(this, android.R.color.black)
        paint.textAlign = Paint.Align.LEFT

        val width = paint.measureText(emoji).toInt()
        val height = paint.fontMetricsInt.run { bottom - top }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawText(emoji, 0f, (-paint.fontMetrics.top), paint)

        return bitmap
    }

    private fun LoadLang() {
        if (LocaleHelper.getLanguage(this).contentEquals(AppConstants.LAN_BN)) {
            binding.toolbar.ivFlag.setImageResource(R.drawable.ic_bd)
        } else {
            binding.toolbar.ivFlag.setImageResource(R.drawable.ic_usa)
        }
    }

    private fun changeLang() {
        if (LocaleHelper.getLanguage(this).contentEquals(AppConstants.LAN_EN)) {
            val mLanguageCode = AppConstants.LAN_BN
            LocaleHelper.setLocale(this@LoginActivity, mLanguageCode)
            binding.toolbar.ivFlag.setImageResource(R.drawable.ic_bd)
            resetTextInViews(mLanguageCode)


        } else if (LocaleHelper.getLanguage(this).contentEquals(AppConstants.LAN_BN)) {
            val mLanguageCode = AppConstants.LAN_EN
            LocaleHelper.setLocale(this@LoginActivity, mLanguageCode)
            binding.toolbar.ivFlag.setImageResource(R.drawable.ic_usa)
            resetTextInViews(mLanguageCode)
        }
    }

    private fun resetTextInViews(mLanguageCode: String) {
        val context = LocaleHelper.setLocale(this, mLanguageCode)
        val resources = context.resources
        binding.toolbar.tvTitle.setText(resources.getString(R.string.login))
        binding.tvCreate.setText(resources.getString(R.string.login_to_your))
        binding.tvAccount.setText(resources.getString(R.string.account))
        binding.etPass.setHint(resources.getString(R.string.password))
        binding.cbRemember.setText(resources.getString(R.string.remember_me))
        binding.tvForgotPass.setText(resources.getString(R.string.forgot_pass) + "?")
        binding.btNext.setText(resources.getString(R.string.login))
        binding.tvLabel.setText(resources.getString(R.string.donot_have_account))
        binding.tvSignup.setText(resources.getString(R.string.register))

        var termsAndConditions =
            "<html>By login, you agree to Yes Parkings's <a href=\"https://yesparkingbd.com/policy.html\">Terms and Conditions</a> and acknowledge that you have read our <a href=\"https://yesparkingbd.com/policy.html\">Privacy Policy</a>.</html>"
        if (mLanguageCode.contentEquals(AppConstants.LAN_BN)) {
            termsAndConditions =
                "<html>লগইন করে, আপনি নীরপথের <a href=\"https://nirapath.com/terms-and-condition.html\">নিয়ম ও শর্তাবলী</a>সাথে একমত এবং স্বীকার করছেন যে আপনি আমাদের <a href=\"https://nirapath.com/privacy-policy.html\">গোপনীয়তা নীতি</a>পড়েছেন।</html>"

        }
        binding.tvTerms.text =
            HtmlCompat.fromHtml(termsAndConditions, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.tvTerms.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }


}