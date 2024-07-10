package com.elahee.testapplication.activity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val networkRepository: NetworkRepository) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val mutableLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
  //  private var lan: String = LocaleHelper.getLanguage(application)
    //private var errorMessage: String = application.getString(R.string.can_not_connect_to_server)


    fun sendLoginRequest(
        hashMap: HashMap<String, String>
    ) {
        compositeDisposable.add(
            networkRepository.requestLogin(
                hashMap
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mutableLiveData.setValue(ApiResponse.loading(null)) }
                .subscribe(
                    { response ->
                        Log.d("ApiTesting", "sendLoginRequest onSuccess ")
                        try {
                            val gson = Gson()
//                            val collectionType = object : TypeToken<CommonResponse?>() {}.type
//                            val commonResponse: CommonResponse = gson.fromJson(Gson().toJson(response), collectionType)
                            if (response.success) {
                                val type = object : TypeToken<LoginResponse?>() {}.type
                                val loginResponse: LoginResponse = gson.fromJson(Gson().toJson(response), type)
                                mutableLiveData.value = ApiResponse.success(loginResponse,response.message,ApiConstants.REQUEST_TYPE_LOGIN)
                            } else {
                                Log.d("ApiTesting","sendLoginRequest onApiFailed errorCode ${response.message} ")
                                mutableLiveData.postValue(ApiResponse.apiFailure(response,response.message,ApiConstants.REQUEST_TYPE_LOGIN))
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.d("ApiTesting","sendLoginRequest exception ${e.localizedMessage} ")
                            mutableLiveData.setValue(ApiResponse.error(null,ApiConstants.ERROR_MESSAGE_INTERNAL_SERVER_ERROR,ApiConstants.REQUEST_TYPE_LOGIN))

                        }
                    },
                    { throwable ->
                        Log.d("ApiTesting", "onError1 : $throwable")
                        sendErrorResponse(throwable)
                    }
                ))
    }

    fun sendSignupRequest(
        hashMap:HashMap<String,String>
    ) {
        compositeDisposable.add(
            networkRepository.requestSignup(
                hashMap
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mutableLiveData.setValue(ApiResponse.loading(null)) }
                .subscribe(
                    { response ->
                        Log.d("ApiTesting", "sendLoginRequest onSuccess ")
                        try {
                            val gson = Gson()
//                            val collectionType = object : TypeToken<CommonResponse?>() {}.type
//                            val commonResponse: CommonResponse = gson.fromJson(Gson().toJson(response), collectionType)
                            if (response.success) {
                                val type = object : TypeToken<SignupResponse?>() {}.type
                                val signupResponse: SignupResponse = gson.fromJson(Gson().toJson(response), type)
                                mutableLiveData.value = ApiResponse.success(signupResponse,response.message,ApiConstants.REQUEST_TYPE_SIGNUP)
                            } else {
                                Log.d("ApiTesting","sendLoginRequest onApiFailed errorCode ${response.message} ")
                                mutableLiveData.postValue(ApiResponse.apiFailure(response,response.message,ApiConstants.REQUEST_TYPE_SIGNUP))
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.d("ApiTesting","sendLoginRequest exception ${e.localizedMessage} ")
                            mutableLiveData.setValue(ApiResponse.error(null,ApiConstants.ERROR_MESSAGE_INTERNAL_SERVER_ERROR,ApiConstants.REQUEST_TYPE_SIGNUP))

                        }
                    },
                    { throwable ->
                        Log.d("ApiTesting", "onError1 : $throwable")
                        sendErrorResponse(throwable)
                    }
                ))
    }

    fun submitOTPRequest(
        hashMap: HashMap<String, String>
    ) {
        compositeDisposable.add(
            networkRepository.requestSubmitOTP(
                hashMap
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mutableLiveData.setValue(ApiResponse.loading(null)) }
                .subscribe(
                    { response ->
                        Log.d("ApiTesting", "sendLoginRequest onSuccess ")
                        try {
                            val gson = Gson()
//                            val collectionType = object : TypeToken<CommonResponse?>() {}.type
//                            val commonResponse: CommonResponse = gson.fromJson(Gson().toJson(response), collectionType)
                            if (response.success) {
                                val type = object : TypeToken<OtpSubmitResponse?>() {}.type
                                val signupResponse: OtpSubmitResponse = gson.fromJson(Gson().toJson(response), type)
                                mutableLiveData.value = ApiResponse.success(signupResponse,response.message,ApiConstants.REQUEST_TYPE_OTP_VRIFY)
                            } else {
                                Log.d("ApiTesting","sendLoginRequest onApiFailed errorCode ${response.message} ")
                                mutableLiveData.postValue(ApiResponse.apiFailure(response,response.message,ApiConstants.REQUEST_TYPE_OTP_VRIFY))
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.d("ApiTesting","sendLoginRequest exception ${e.localizedMessage} ")
                            mutableLiveData.setValue(ApiResponse.error(null,ApiConstants.ERROR_MESSAGE_INTERNAL_SERVER_ERROR,ApiConstants.REQUEST_TYPE_OTP_VRIFY))

                        }
                    },
                    { throwable ->
                        Log.d("ApiTesting", "onError1 : $throwable")
                        sendErrorResponse(throwable)
                    }
                ))
    }

    fun reSendOTPRequest(
        hashMap: HashMap<String, String>
    ) {
        compositeDisposable.add(
            networkRepository.requestResendOTP(
                hashMap
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mutableLiveData.setValue(ApiResponse.loading(null)) }
                .subscribe(
                    { response ->
                        Log.d("ApiTesting", "sendLoginRequest onSuccess ")
                        try {
                            val gson = Gson()
//                            val collectionType = object : TypeToken<CommonResponse?>() {}.type
//                            val commonResponse: CommonResponse = gson.fromJson(Gson().toJson(response), collectionType)
                            if (response.success) {
                                val type = object : TypeToken<CommonResponse?>() {}.type
                                val signupResponse: CommonResponse = gson.fromJson(Gson().toJson(response), type)
                                mutableLiveData.value = ApiResponse.success(signupResponse,response.message,ApiConstants.REQUEST_TYPE_OTP_RESEND)
                            } else {
                                Log.d("ApiTesting","sendLoginRequest onApiFailed errorCode ${response.message} ")
                                mutableLiveData.postValue(ApiResponse.apiFailure(response,response.message,ApiConstants.REQUEST_TYPE_OTP_RESEND))
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.d("ApiTesting","sendLoginRequest exception ${e.localizedMessage} ")
                            mutableLiveData.setValue(ApiResponse.error(null,ApiConstants.ERROR_MESSAGE_INTERNAL_SERVER_ERROR,ApiConstants.REQUEST_TYPE_OTP_RESEND))

                        }
                    },
                    { throwable ->
                        Log.d("ApiTesting", "onError1 : $throwable")
                        sendErrorResponse(throwable)
                    }
                ))
    }

    fun reForgotPassOTPRequest(
        hashMap: HashMap<String, String>
    ) {
        compositeDisposable.add(
            networkRepository.requestForgotPassOTP(
                hashMap
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mutableLiveData.setValue(ApiResponse.loading(null)) }
                .subscribe(
                    { response ->
                        Log.d("ApiTesting", "sendLoginRequest onSuccess ")
                        try {
                            val gson = Gson()
//                            val collectionType = object : TypeToken<CommonResponse?>() {}.type
//                            val commonResponse: CommonResponse = gson.fromJson(Gson().toJson(response), collectionType)
                            if (response.success) {
                                val type = object : TypeToken<ForgotPassOtpResponse?>() {}.type
                                val forgotPassOtpRes: ForgotPassOtpResponse = gson.fromJson(Gson().toJson(response), type)
                                mutableLiveData.value = ApiResponse.success(forgotPassOtpRes,response.message,ApiConstants.REQUEST_TYPE_FORGOT_PASS_OTP)
                            } else {
                                Log.d("ApiTesting","sendLoginRequest onApiFailed errorCode ${response.message} ")
                                mutableLiveData.postValue(ApiResponse.apiFailure(response,response.message,ApiConstants.REQUEST_TYPE_FORGOT_PASS_OTP))
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.d("ApiTesting","sendLoginRequest exception ${e.localizedMessage} ")
                            mutableLiveData.setValue(ApiResponse.error(null,ApiConstants.ERROR_MESSAGE_INTERNAL_SERVER_ERROR,ApiConstants.REQUEST_TYPE_FORGOT_PASS_OTP))

                        }
                    },
                    { throwable ->
                        Log.d("ApiTesting", "onError1 : $throwable")
                        sendErrorResponse(throwable)
                    }
                ))
    }

    fun reqForgotPassSubmitOTPRequest(
        hashMap: HashMap<String, String>
    ) {
        compositeDisposable.add(
            networkRepository.requestForgotPassSubmitOTP(
                hashMap
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mutableLiveData.setValue(ApiResponse.loading(null)) }
                .subscribe(
                    { response ->
                        Log.d("ApiTesting", "sendLoginRequest onSuccess ")
                        try {
                            val gson = Gson()
//                            val collectionType = object : TypeToken<CommonResponse?>() {}.type
//                            val commonResponse: CommonResponse = gson.fromJson(Gson().toJson(response), collectionType)
                            if (response.success) {
                                val type = object : TypeToken<CommonResponse?>() {}.type
                                val commonResponse: CommonResponse = gson.fromJson(Gson().toJson(response), type)
                                mutableLiveData.value = ApiResponse.success(commonResponse,response.message,ApiConstants.REQUEST_TYPE_FORGOT_PASS_SUBMIT_OTP)
                            } else {
                                Log.d("ApiTesting","sendLoginRequest onApiFailed errorCode ${response.message} ")
                                mutableLiveData.postValue(ApiResponse.apiFailure(response,response.message,ApiConstants.REQUEST_TYPE_FORGOT_PASS_SUBMIT_OTP))
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.d("ApiTesting","sendLoginRequest exception ${e.localizedMessage} ")
                            mutableLiveData.setValue(ApiResponse.error(null,ApiConstants.ERROR_MESSAGE_INTERNAL_SERVER_ERROR,ApiConstants.REQUEST_TYPE_FORGOT_PASS_SUBMIT_OTP))

                        }
                    },
                    { throwable ->
                        Log.d("ApiTesting", "onError1 : $throwable")
                        sendErrorResponse(throwable)
                    }
                ))
    }

    fun reqForgotPassReSendOTPRequest(
        hashMap: HashMap<String, String>
    ) {
        compositeDisposable.add(
            networkRepository.requestForgotPassReSendOTP(
                hashMap
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mutableLiveData.setValue(ApiResponse.loading(null)) }
                .subscribe(
                    { response ->
                        Log.d("ApiTesting", "sendLoginRequest onSuccess ")
                        try {
                            val gson = Gson()
//                            val collectionType = object : TypeToken<CommonResponse?>() {}.type
//                            val commonResponse: CommonResponse = gson.fromJson(Gson().toJson(response), collectionType)
                            if (response.success) {
                                val type = object : TypeToken<CommonResponse?>() {}.type
                                val commonResponse: CommonResponse = gson.fromJson(Gson().toJson(response), type)
                                mutableLiveData.value = ApiResponse.success(commonResponse,response.message,ApiConstants.REQUEST_TYPE_OTP_RESEND)
                            } else {
                                Log.d("ApiTesting","sendLoginRequest onApiFailed errorCode ${response.message} ")
                                mutableLiveData.postValue(ApiResponse.apiFailure(response,response.message,ApiConstants.REQUEST_TYPE_OTP_RESEND))
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.d("ApiTesting","sendLoginRequest exception ${e.localizedMessage} ")
                            mutableLiveData.setValue(ApiResponse.error(null,ApiConstants.ERROR_MESSAGE_INTERNAL_SERVER_ERROR,ApiConstants.REQUEST_TYPE_OTP_RESEND))

                        }
                    },
                    { throwable ->
                        Log.d("ApiTesting", "onError1 : $throwable")
                        sendErrorResponse(throwable)
                    }
                ))
    }

    fun sendSetPassRequest(
        hashMap: HashMap<String, String>
    ) {
        compositeDisposable.add(
            networkRepository.requestForgotPassSETRequest(
                hashMap
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mutableLiveData.setValue(ApiResponse.loading(null)) }
                .subscribe(
                    { response ->
                        Log.d("ApiTesting", "sendLoginRequest onSuccess ")
                        try {
                            val gson = Gson()
//                            val collectionType = object : TypeToken<CommonResponse?>() {}.type
//                            val commonResponse: CommonResponse = gson.fromJson(Gson().toJson(response), collectionType)
                            if (response.success) {
                                val type = object : TypeToken<CommonResponse?>() {}.type
                                val commonResponse: CommonResponse = gson.fromJson(Gson().toJson(response), type)
                                mutableLiveData.value = ApiResponse.success(commonResponse,response.message,ApiConstants.REQUEST_TYPE_UPDATE_PASSWORD)
                            } else {
                                Log.d("ApiTesting","sendLoginRequest onApiFailed errorCode ${response.message} ")
                                mutableLiveData.postValue(ApiResponse.apiFailure(response,response.message,ApiConstants.REQUEST_TYPE_UPDATE_PASSWORD))
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.d("ApiTesting","sendLoginRequest exception ${e.localizedMessage} ")
                            mutableLiveData.setValue(ApiResponse.error(null,ApiConstants.ERROR_MESSAGE_INTERNAL_SERVER_ERROR,ApiConstants.REQUEST_TYPE_UPDATE_PASSWORD))

                        }
                    },
                    { throwable ->
                        Log.d("ApiTesting", "onError1 : $throwable")
                        sendErrorResponse(throwable)
                    }
                ))
    }

    private fun sendErrorResponse(throwable: Throwable) {
        when (throwable) {
            is NoInternetException -> {
                Log.d("ApiTesting", "Internet not available")
                mutableLiveData.value = ApiResponse.error(
                    throwable,
                    ApiConstants.ERROR_MESSAGE_NO_INTERNET,
                    null
                )
            }
            is HttpException -> {
//                val errorMessage = CommonErrorHandler.getErrorMessage(throwable)
                Log.d("ApiTesting", "onError2 : $throwable")
                mutableLiveData.value = ApiResponse.error(
                    throwable,
                    "errorMessage",
                    null
                )
            }
            else -> {
                mutableLiveData.value = ApiResponse.error(
                    throwable,
                    "errorMessage",
                    null
                )
            }
        }
    }


    fun getSignInResponse(): MutableLiveData<ApiResponse> {
        return mutableLiveData
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}