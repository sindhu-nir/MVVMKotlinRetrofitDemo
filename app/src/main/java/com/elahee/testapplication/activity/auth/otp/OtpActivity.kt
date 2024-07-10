package com.btracsolutions.yesparking.activity.auth.otp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider

import com.btracsolutions.yesparking.activity.BaseActivity
import com.btracsolutions.yesparking.activity.auth.AuthViewModel
import com.btracsolutions.yesparking.activity.auth.setpassword.ChangePassActivity
import com.btracsolutions.yesparking.activity.mandatoryentries.MandatoryEntriesActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityOtpBinding
    var userID = ""
    var phone = ""
    private lateinit var viewModel: AuthViewModel
    var isForgot = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
        statusBarColor(R.color.white)

        getIntentData()
        initView()
        startTimer()
        initListeners()
        initViewModel()

    }

    var timer: CountDownTimer? = null
    private fun startTimer() {
        try {
            if (timer != null) {
                timer!!.cancel()
            }
            timer = object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    var seconds: Long = millisUntilFinished / 1000
                    binding.tvOtpTimer.text = "" + seconds + "s"
                }

                override fun onFinish() {
                    binding.tvOtpTimer.text = ""
                    binding.tvOtpResendLabel.text = ""
                    binding.tvOtpResend.setTextColor(getColor(R.color.colorPrimary))
                    binding.tvOtpResend.isEnabled = true
                }
            }
            (timer as CountDownTimer).start()
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

    private fun getIntentData() {
        try {
            if (getIntent() != null) {
//                loginData = Gson().fromJson(
//                    intent.getStringExtra(PrefKey.USER),
//                    object : TypeToken<LoginData?>() {}.type
//                ) as LoginData
                if (intent.hasExtra(PrefKey.USER_ID)) {
                    userID = intent.getStringExtra(PrefKey.USER_ID).toString()
                }
                if (intent.hasExtra(PrefKey.PHONE)) {
                    phone = intent.getStringExtra(PrefKey.PHONE).toString()
                }
                if (intent.hasExtra("isForgot")) {
                    isForgot = intent.getBooleanExtra("isForgot", false)
                }

            }
        } catch (e: Exception) {

        }
    }

    private fun initView() {
        binding.toolbar.tvTitle.text = getString(R.string.otp)
        binding.toolbar.tvTitle.isAllCaps = true

        binding.tvOtpResend.setTextColor(getColor(R.color.textview_color_black))
        if (isForgot){
            binding.tvOtpsendto.setText(getString(R.string.otp_has_been_sent_to) + " +880"+phone.replaceRange(3,7,"****"))
        }else {
            binding.tvOtpsendto.setText(getString(R.string.otp_has_been_sent_to) + " " + phone.replaceRange(7,11,"****"))
        }
        binding.tvOtpResendLabel.setText("in")
        if (isForgot) {
            binding.btSubmit.text = getString(R.string.verify)
        }
        // binding?.toolbar?.llBack?.setOnClickListener(this)


    }

    private fun initListeners() {
        binding?.btSubmit?.setOnClickListener(this)
        binding?.tvOtpResend?.setOnClickListener(this)
        binding?.toolbar?.llBack?.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding?.btSubmit?.id -> {
                if (binding.pinView.text.toString().isNullOrBlank()) {
                    showToast(getString(R.string.invalid_otp))
                } else if (binding.pinView.text.toString().length < 4) {
                    showToast(getString(R.string.otp_must_be_4_digits))
                } else {
                    submitOTPNow(binding.pinView.text.toString())
                }

            }

            binding?.tvOtpResend?.id -> {
                resendOTP()
            }

            binding?.toolbar?.llBack?.id -> {
                finish()
            }
        }
    }


    private fun resendOTP(
    ) {
        val hashMap = HashMap<String, String>()
        hashMap["userId"] = userID
        if (isForgot){
            hashMap["countryCode"] = "+880"
            hashMap["mobileNumber"] = phone
            viewModel.reqForgotPassReSendOTPRequest(hashMap)

        }else {
            viewModel.reSendOTPRequest(
                hashMap
            )
        }
    }

    private fun submitOTPNow(
        otp: String
    ) {
        val hashMap = HashMap<String, String>()
        hashMap["userId"] = userID
        hashMap["otp"] = otp

        if (isForgot) {
            hashMap["countryCode"] = "+880"
            hashMap["mobileNumber"] = phone
            viewModel.reqForgotPassSubmitOTPRequest(
                hashMap
            )
        } else {
            viewModel.submitOTPRequest(
                hashMap
            )
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

                if (apiResponse.requestType!!.equals(ApiConstants.REQUEST_TYPE_OTP_VRIFY)) {
                    try {
                        val successResponse = apiResponse.data as OtpSubmitResponse
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
                            val intent =
                                Intent(this@OtpActivity, MandatoryEntriesActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                                    or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            finish()

                        } else {
                            showToast(successResponse.message)
                        }
                    } catch (e: Exception) {
                        println(e.message)
                    }


                } else if (apiResponse.requestType!!.equals(ApiConstants.REQUEST_TYPE_OTP_RESEND)) {
                    try {
                        val successResponse = apiResponse.data as CommonResponse
                        if (successResponse.success) {
                            showToast(getString(R.string.resend_otp_successfull))
                            binding.tvOtpResendLabel.text = getString(R.string.otp_in)
                            binding.tvOtpResend.setTextColor(getColor(R.color.textview_color_black))
                            binding.tvOtpResend.isEnabled = false
                            startTimer()
                        } else {
                            showToast(successResponse.message)
                        }
                    } catch (e: Exception) {
                        println(e.message)
                    }
                } else if (apiResponse.requestType!!.equals(ApiConstants.REQUEST_TYPE_FORGOT_PASS_SUBMIT_OTP)) {
                    try {
                        val successResponse = apiResponse.data as CommonResponse
                        if (successResponse.success) {
                            val intent=Intent(this@OtpActivity,ChangePassActivity::class.java)
                            intent.putExtra(PrefKey.USER_ID,userID)
                            intent.putExtra("otp",binding.pinView.text.toString())
                            startActivity(intent)
                            finish()

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
                if (apiResponse.requestType!! == ApiConstants.REQUEST_TYPE_OTP_VRIFY) {
                    showToast(apiResponse.message)
                    // handleErrorResponse(commonResponse)
                } else if (apiResponse.requestType!! == ApiConstants.REQUEST_TYPE_OTP_RESEND) {
                    showToast(apiResponse.message)
                    // handleErrorResponse(commonResponse)
                }else if (apiResponse.requestType!! == ApiConstants.REQUEST_TYPE_FORGOT_PASS_SUBMIT_OTP) {
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

    override fun onDestroy() {
        super.onDestroy()
        if (timer != null) {
            timer!!.cancel()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }
}