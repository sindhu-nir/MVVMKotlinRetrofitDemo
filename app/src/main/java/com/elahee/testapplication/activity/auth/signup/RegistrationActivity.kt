package com.btracsolutions.yesparking.activity.auth.signup

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.text.HtmlCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.btracsolutions.yesparking.activity.BaseActivity
import com.btracsolutions.yesparking.activity.auth.AuthViewModel
import com.btracsolutions.yesparking.activity.auth.login.LoginActivity
import com.btracsolutions.yesparking.activity.auth.otp.OtpActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegistrationActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var viewModel: AuthViewModel
    var fcmid=""
    var isEyePassOpen= false
    var isEyeConfirmPassOpen= false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
        statusBarColor(R.color.white)


        initView()
        initListeners()
        initViewModel()
        getFCMToken()
        onBackPressed_()

//        if (BuildConfig.DEBUG){
//            binding.tvName.setText("Syed Reazul Elahee")
//            binding.tvPhone.setText("01700704427")
//            binding.tvEmail.setText("sindhu.nir180@gmail.com")
//            binding.tvPass.setText("qqqqqq")
//            binding.tvConfirmPass.setText("qqqqqq")
//        }
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

                // Log and toast
                //val msg = getString(R.string.msg_token_fmt, token)
                // Log.d(TAG, msg)
            })
        }else{
            fcmid=MySharedPref.getString(PrefKey.FCM_DEVICE_TOKEN).toString()
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        viewModel.getSignInResponse().observe(this, {
            consumeResponse(it)
        })

    }

    private fun initView() {
        binding.toolbar.tvTitle.text = getString(R.string.registration)
        var termsAndConditions =
            "<html>By login, you agree to Yes Parkings's <a href=\"https://yesparkingbd.com/terms-and-conditions.html\">Terms and Conditions</a> and acknowledge that you have read our <a href=\"https://yesparkingbd.com/policy.html\">Privacy Policy</a>.</html>"

        if (LocaleHelper.getLanguage(this).contentEquals(AppConstants.LAN_BN)){
            termsAndConditions =
                "<html>লগইন করে, আপনি ইয়েস পার্কিং এর <a href=\"https://yesparkingbd.com.com/terms-and-conditions.html\">নিয়ম ও শর্তাবলী</a>সাথে একমত এবং স্বীকার করছেন যে আপনি আমাদের <a href=\"https://yesparkingbd.com/policy.html\">গোপনীয়তা নীতি</a>পড়েছেন।</html>"


        }

        binding?.tvTerms?.text = HtmlCompat.fromHtml(termsAndConditions, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.tvTerms.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun initListeners() {
        binding?.btNext?.setOnClickListener(this)
        binding?.ivEyePass?.setOnClickListener(this)
        binding?.ivEyeConfirmPass?.setOnClickListener(this)
        binding?.toolbar?.llBack?.setOnClickListener(this)
        binding?.tvLogin?.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding?.btNext?.id -> {
                Utils.preventTwoClick(v!!)
                if (validateData()) {
                    var substractPhone=isPhoneStartsWithZero(binding.tvPhone.text.toString())
                    signUpNow(binding.tvName.text.toString(),
                        "+880",
                        substractPhone,
                        binding.tvEmail.text.toString(),
                        binding.tvPass.text.toString(),
                        "Android",
                        fcmid)
                }
            }
            binding?.toolbar?.llBack?.id -> {
                val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                // intent.putExtra("news",Gson().toJson(data).toString())
                startActivity(intent)
                finish()
            }
            binding?.tvLogin?.id -> {
                val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                // intent.putExtra("news",Gson().toJson(data).toString())
                startActivity(intent)
                finish()
            }

            binding?.ivEyePass?.id -> {
                if (isEyePassOpen){
                    isEyePassOpen=false
                    binding.ivEyePass.setImageResource(R.drawable.ic_eye_close)
                    binding.tvPass.inputType= InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
                    binding.tvPass.setTransformationMethod(PasswordTransformationMethod.getInstance())

                }else{
                    isEyePassOpen=true
                    binding.ivEyePass.setImageResource(R.drawable.ic_eye_open)
                    binding.tvPass.inputType= InputType.TYPE_CLASS_TEXT
                    binding.tvPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance())

                }
            }

            binding?.ivEyeConfirmPass?.id -> {
                if (isEyeConfirmPassOpen){
                    isEyeConfirmPassOpen=false
                    binding.ivEyeConfirmPass.setImageResource(R.drawable.ic_eye_close)
                    binding.tvConfirmPass.inputType= InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
                    binding.tvConfirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance())

                }else{
                    isEyeConfirmPassOpen=true
                    binding.ivEyeConfirmPass.setImageResource(R.drawable.ic_eye_open)
                    binding.tvConfirmPass.inputType= InputType.TYPE_CLASS_TEXT
                    binding.tvConfirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance())

                }
            }
        }
    }

    private fun isPhoneStartsWithZero(phone: String): String {
        var substractPhone=""
        if (phone.startsWith("0")){
            substractPhone= phone.substring(1, phone.length)
        }else{
            substractPhone= phone
        }
        return substractPhone
    }

    private fun validateData(): Boolean {
        if (binding.tvName.text.toString().isNullOrBlank()){
            showToast(getString(R.string.invalid_name))
            return false
        }
        if (binding.tvName.text?.length!! <3){
            showToast(getString(R.string.msg_txt_username_3_char))
            return false
        }
//        if (binding.tvEmail.text.toString().isNullOrBlank() || !Patterns.EMAIL_ADDRESS.matcher(binding.tvEmail.text.toString()).matches()){
//            showToast(getString(R.string.invalid_email))
//            return false
//        }
        if (binding.tvPhone.text.toString().isNullOrBlank() || binding.tvPhone.text.toString().length<10 || binding.tvPhone.text.toString().length>11){
            showToast(getString(R.string.invalid_mobile))
            return false
        }
        if (binding.tvPass.text.toString().isNullOrBlank()) {
            showToast(getString(R.string.msg_txt_enter_pass))
            return false
        }
        if (binding.tvPass.text.toString().length<6){
            showToast(getString(R.string.msg_txt_pass_six_digits))
            return false
        }

        else if (!binding.tvPass.text.contentEquals(binding.tvConfirmPass.text)){
            showToast(getString(R.string.msg_txt_enter_pass_cpass_mismatch))
            return false
        }

        return true
    }

    private fun signUpNow(
        name: String,
        countryCode: String,
        mobileNumber: String,
        email: String,
        password: String,
        deviceType: String,
        deviceKey: String
    ) {
        if (NetworkConnectivityChecker.isOnline()) {
            val hashMap = HashMap<String, String>()
            hashMap["name"] = name
            hashMap["countryCode"] = countryCode
            hashMap["mobileNumber"] = mobileNumber
            hashMap["email"] = email
            hashMap["password"] = password
            hashMap["deviceType"] = deviceType
            hashMap["deviceKey"] = deviceKey

            viewModel.sendSignupRequest(
                hashMap
            )
        }else{
            showToast(getString(R.string.internet_not_available))
        }
    }

    private fun consumeResponse(apiResponse: ApiResponse) {
        Log.d("ApiTesting", "consumeResponse 121")
        when (apiResponse.status) {
            Status.LOADING -> {
                showProgressBar()
            }

            Status.SUCCESS -> {
                hideProgressBar()

                if (apiResponse.requestType!!.equals(ApiConstants.REQUEST_TYPE_SIGNUP)) {
                    try {
                        val successResponse = apiResponse.data as SignupResponse
                        if (successResponse.success) {
                            val intent = Intent(this@RegistrationActivity, OtpActivity::class.java)
                            intent.putExtra(PrefKey.PHONE, ""+successResponse.data.countryCode+successResponse.data.mobileNumber)
                            intent.putExtra(PrefKey.USER_ID, successResponse.data._id)
                            startActivity(intent)
                           // finish()

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
                if (apiResponse.requestType!! == ApiConstants.REQUEST_TYPE_SIGNUP) {
                    val successResponse = apiResponse.data as SignupResponse
                    Log.d(
                        "ApiTesting",
                        "consumeResponse ${successResponse.message} errorCode: ${successResponse.message}"
                    )
                    showToast(successResponse.message)
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
    fun onBackPressed_() {

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                // intent.putExtra("news",Gson().toJson(data).toString())
                startActivity(intent)
                finish()
            }
        })

    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

}