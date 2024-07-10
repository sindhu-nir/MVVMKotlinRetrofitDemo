package com.btracsolutions.yesparking.activity.auth.forgotpass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.btracsolutions.yesparking.activity.BaseActivity
import com.btracsolutions.yesparking.activity.auth.AuthViewModel
import com.btracsolutions.yesparking.activity.auth.login.LoginActivity
import com.btracsolutions.yesparking.activity.auth.otp.OtpActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPassActivity : BaseActivity(),View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)

        initView()
        initListeners()
        initViewModel()
        onBackPressed_()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        viewModel.getSignInResponse().observe(this, {
            consumeResponse(it)
        })

    }

    private fun initView() {
        val termsAndConditions =
            "<html>By login, you agree to Yes Parkings's <a href=\"https://yesparkingbd.com/policy.html\">Terms and Conditions</a> and acknowledge that you have read our <a href=\"https://yesparkingbd.com/policy.html\">Privacy Policy</a>.</html>"



        binding.tvTerms.text = HtmlCompat.fromHtml(termsAndConditions, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.tvTerms.movementMethod = LinkMovementMethod.getInstance()
        binding.tvTerms.visibility=View.GONE
        binding.toolbar.tvTitle.text = getString(R.string.forgot_pass)
        binding.tvCreate.text = getString(R.string.reset)
        binding.tvAccount.text = getString(R.string.password)
        binding.etPass.visibility = View.GONE
        binding.cbRemember.visibility = View.GONE
        binding.tvForgotPass.visibility = View.GONE
        binding.ivEye.visibility = View.GONE
        binding.btNext.text = getString(R.string.submit)
        binding.tvSignup.text=getString(R.string.login)
        binding.tvLabel.text=getString(R.string.already_have_account)
        //binding.toolbar.llBack.visibility= View.GONE
    }

    private fun initListeners() {
        binding?.btNext?.setOnClickListener(this)
        binding?.tvSignup?.setOnClickListener(this)
        binding?.ivEye?.setOnClickListener(this)
//        binding?.btSkip?.setOnClickListener(this)
        binding?.toolbar?.llBack?.setOnClickListener(this)


    }

    var substractPhone=""
    override fun onClick(v: View?) {
        when (v?.id) {
            binding?.btNext?.id -> {
                Utils.preventTwoClick(v!!)
                if (validateData()) {
                    substractPhone = isPhoneStartsWithZero(binding.etPhone.text.toString())
                    forgotPassOtpReq(substractPhone)
                }
            }
            binding?.toolbar?.llBack?.id -> {

                val intent = Intent(this@ForgotPassActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            binding?.tvSignup?.id -> {

                val intent = Intent(this@ForgotPassActivity, LoginActivity::class.java)
                // intent.putExtra("news",Gson().toJson(data).toString())
                startActivity(intent)
                finish()
            }
        }
    }

    private fun forgotPassOtpReq(mobile: String) {
        if (NetworkConnectivityChecker.isOnline()) {
            val hashMap = HashMap<String, String>()
            hashMap["countryCode"] = "+880"
            hashMap["mobileNumber"] = mobile


            viewModel.reForgotPassOTPRequest(hashMap)
        } else {
            showToast(getString(R.string.internet_not_available))
        }
        //viewModel.sendLoginRequest(userName, password)
    }

    private fun consumeResponse(apiResponse: ApiResponse) {
        Log.d("ApiTesting", "consumeResponse 121")
        when (apiResponse.status) {
            Status.LOADING -> {
                showProgressBar()
            }

            Status.SUCCESS -> {
                hideProgressBar()

                if (apiResponse.requestType!!.equals(ApiConstants.REQUEST_TYPE_FORGOT_PASS_OTP)) {
                    try {
                        val successResponse = apiResponse.data as ForgotPassOtpResponse
                        if (successResponse.success) {
                            val userid=successResponse.data.userId
                            val intent=Intent(this@ForgotPassActivity,OtpActivity::class.java)
                            intent.putExtra(PrefKey.USER_ID,userid)
                            intent.putExtra(PrefKey.PHONE,substractPhone)
                            intent.putExtra("isForgot",true)
                            startActivity(intent)
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
                if (apiResponse.requestType!! == ApiConstants.REQUEST_TYPE_FORGOT_PASS_OTP) {
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
        var substractPhone=""
        if (phone.startsWith("0")){
            substractPhone= phone.substring(1, phone.length)
        }else{
            substractPhone= phone
        }
        return substractPhone
    }

    private fun validateData(): Boolean {
        if (binding.etPhone.text.toString()
                .isNullOrBlank() || binding.etPhone.text.toString().length < 10 || binding.etPhone.text.toString().length > 11
        ) {
            showToast(getString(R.string.invalid_mobile))
            return false
        }

        return true
    }

    fun onBackPressed_() {

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@ForgotPassActivity, LoginActivity::class.java)
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