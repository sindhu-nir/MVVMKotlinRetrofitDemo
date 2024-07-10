package com.btracsolutions.yesparking.activity.auth.setpassword

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.btracsolutions.yesparking.activity.BaseActivity
import com.btracsolutions.yesparking.activity.auth.AuthViewModel
import com.btracsolutions.yesparking.activity.auth.login.LoginActivity
import com.btracsolutions.yesparking.activity.mandatoryentries.MandatoryEntriesActivity

import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePassActivity : BaseActivity() , View.OnClickListener{

    private lateinit var binding: ActivityChangePasswordBinding
    var userID = ""
    var otp = ""
    private lateinit var viewModel: AuthViewModel
    var isEyePassOpen= false
    var isEyeConfirmPassOpen= false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
        statusBarColor(R.color.white)

        getIntentData()
        initView()
        initListeners()
        initViewModel()
    }

    private fun getIntentData() {
        try {
            if (getIntent() != null) {
                if (intent.hasExtra(PrefKey.USER_ID)) {
                    userID = intent.getStringExtra(PrefKey.USER_ID).toString()
                }
                if (intent.hasExtra("otp")) {
                    otp = intent.getStringExtra("otp").toString()
                }


            }
        } catch (e: Exception) {
            println(e.message)
        }
    }
    private fun initView() {
        binding.toolbar.tvTitle.text = getString(R.string.set_new_pass)
        binding.toolbar.tvTitle.isAllCaps = false

        binding.etOldPass.visibility=View.GONE
        binding.tvCreate.text=getString(R.string.set_a_new)
        binding.tvAccount.text=getString(R.string.password)
        binding.btSubmit.text=getString(R.string.update)
    }

    private fun initListeners() {
        binding?.btSubmit?.setOnClickListener(this)
        binding?.toolbar?.llBack?.setOnClickListener(this)
        binding?.ivNewEyePass?.setOnClickListener(this)
        binding?.ivEyeConfirmPass?.setOnClickListener(this)

    }

    private fun updatePass(password: String) {
        if (NetworkConnectivityChecker.isOnline()) {
            val hashMap = HashMap<String, String>()
            hashMap["userId"] = userID
            hashMap["password"] = password
            hashMap["otp"] = otp


            viewModel.sendSetPassRequest(hashMap)
        } else {
            showToast(getString(R.string.internet_not_available))
        }
        //viewModel.sendLoginRequest(userName, password)
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            binding?.btSubmit?.id -> {

                if (validateData()){
                    updatePass(binding.etConfirmPass.text.toString())
                }

            }


            binding?.toolbar?.llBack?.id -> {
                gotoLogin()
            }
            binding?.ivNewEyePass?.id -> {
                if (isEyePassOpen){
                    isEyePassOpen=false
                    binding.ivNewEyePass.setImageResource(R.drawable.ic_eye_close)
                    binding.etNewPass.inputType= InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
                    binding.etNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance())

                }else{
                    isEyePassOpen=true
                    binding.ivNewEyePass.setImageResource(R.drawable.ic_eye_open)
                    binding.etNewPass.inputType= InputType.TYPE_CLASS_TEXT
                    binding.etNewPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance())

                }
            }

            binding?.ivEyeConfirmPass?.id -> {
                if (isEyeConfirmPassOpen){
                    isEyeConfirmPassOpen=false
                    binding.ivEyeConfirmPass.setImageResource(R.drawable.ic_eye_close)
                    binding.etConfirmPass.inputType= InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
                    binding.etConfirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance())

                }else{
                    isEyeConfirmPassOpen=true
                    binding.ivEyeConfirmPass.setImageResource(R.drawable.ic_eye_open)
                    binding.etConfirmPass.inputType= InputType.TYPE_CLASS_TEXT
                    binding.etConfirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance())

                }
            }

        }
    }
    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        viewModel.getSignInResponse().observe(this, {
            consumeResponse(it)
        })

    }

    private fun consumeResponse(apiResponse: ApiResponse) {
        Log.d("ApiTesting", "consumeResponse 121")
        when (apiResponse.status) {
            Status.LOADING -> {
                showProgressBar()
            }

            Status.SUCCESS -> {
                hideProgressBar()

                try {
                    if (apiResponse.requestType!!.equals(ApiConstants.REQUEST_TYPE_UPDATE_PASSWORD)) {
                        val successResponse = apiResponse.data as CommonResponse
                        if (successResponse.success) {
                            showToast(successResponse.message)
                            val intent =
                                Intent(this@ChangePassActivity, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                                    or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            finish()
                        }else
                        {
                            showToast(successResponse.message)
                        }


                    }
                } catch (e: Exception) {
                    println(e.message)
                }
            }

            Status.FAILED -> {
                hideProgressBar()
                if (apiResponse.requestType!! == ApiConstants.REQUEST_TYPE_UPDATE_PASSWORD) {
                    showToast(apiResponse.message)
                    // handleErrorResponse(commonResponse)
                } else if (apiResponse.requestType!! == ApiConstants.REQUEST_TYPE_OTP_RESEND) {
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

    private fun validateData(): Boolean {

        if (binding.etNewPass.text.toString().isNullOrBlank()) {
            showToast(getString(R.string.msg_txt_enter_pass))
            return false
        }
        if (binding.etNewPass.text.toString().length<6){
            showToast(getString(R.string.msg_txt_pass_six_digits))
            return false
        }

        else if (!binding.etNewPass.text.contentEquals(binding.etConfirmPass.text)){
            showToast(getString(R.string.msg_txt_enter_pass_cpass_mismatch))
            return false
        }

        return true
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }


    fun gotoLogin(){
        val intent = Intent(this@ChangePassActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
    fun onBackPressed_() {

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                gotoLogin()
            }
        })

    }
}