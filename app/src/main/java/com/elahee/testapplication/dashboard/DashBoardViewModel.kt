package com.elahee.testapplication.dashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.util.HashMap
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(private val networkRepository: NetworkRepository) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val mutableLiveData: MutableLiveData<ApiResponse> = MutableLiveData<ApiResponse>()
    //  private var lan: String = LocaleHelper.getLanguage(application)
    // private var errorMessage: String = application.getString(R.string.can_not_connect_to_server)



    fun getParkingData(authorization: String)
    {
        viewModelScope.launch {
            mutableLiveData.setValue(ApiResponse.loading(null))
            networkRepository.requestParkingData(authorization)
                .catch { e->
                    Log.d("main", "getPost: ${e.message}")
                    mutableLiveData.setValue(
                        ApiResponse.error(null,
                            ApiConstants.ERROR_MESSAGE_INTERNAL_SERVER_ERROR,
                            ApiConstants.REQUEST_TYPE_PARKING_SPAPCES))
                }

                .collect {response->
                    if (response.isSuccessful && response.body()?.success == true){
                        mutableLiveData.value = ApiResponse.success(
                            response.body()!!,response.body()?.message,
                            ApiConstants.REQUEST_TYPE_PARKING_SPAPCES)
                    }else{
                        mutableLiveData.postValue(
                            ApiResponse.apiFailure(response.body(),response.body()?.message,
                                ApiConstants.REQUEST_TYPE_PARKING_SPAPCES))
                    }
//                    mutableLiveData.value=memberDataList
                }


        }
    }


    fun sendDataSyncReq(auth:String,userid:String)
    {
        viewModelScope.launch {
            mutableLiveData.setValue(ApiResponse.loading(null))
            networkRepository.syncParkingData(auth,userid)
                .catch { e->
                    Log.d("main", "getPost: ${e.message}")
                    mutableLiveData.setValue(
                        ApiResponse.error(null,
                            ApiConstants.ERROR_MESSAGE_INTERNAL_SERVER_ERROR,
                            ApiConstants.REQUEST_TYPE_DATA_SYNC))
                }

                .collect {response->
                    if (response.isSuccessful && response.body()?.success == true){
                        mutableLiveData.value = ApiResponse.success(
                            response.body()!!,response.body()?.message,
                            ApiConstants.REQUEST_TYPE_DATA_SYNC)
                    }else{
                        mutableLiveData.postValue(
                            ApiResponse.apiFailure(response.body(),response.body()?.message,
                                ApiConstants.REQUEST_TYPE_DATA_SYNC))
                    }
//                    mutableLiveData.value=memberDataList
                }


        }
    }

    fun sendLogoutRequest(
        token:String,
        hashMap: HashMap<String, String>
    ) {
        compositeDisposable.add(
            networkRepository.requestLogOut(
                token,
                hashMap
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mutableLiveData.setValue(ApiResponse.loading(null)) }
                .subscribe(
                    { response ->
                        Log.d("ApiTesting", "sendLOGOUTRequest onSuccess ")
                        try {
                            val gson = Gson()
                            if (response.success) {
                                val type = object : TypeToken<CommonResponse?>() {}.type
                                val commonResponse: CommonResponse = gson.fromJson(Gson().toJson(response), type)
                                mutableLiveData.value = ApiResponse.success(commonResponse,response.message,ApiConstants.REQUEST_TYPE_LOGOUT)
                            } else {
                                Log.d("ApiTesting","sendLOGOUTRequest onApiFailed errorCode ${response.message} ")
                                mutableLiveData.postValue(ApiResponse.apiFailure(response,response.message,ApiConstants.REQUEST_TYPE_LOGOUT))
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.d("ApiTesting","sendLOGOUTRequest exception ${e.localizedMessage} ")
                            mutableLiveData.setValue(ApiResponse.error(null,ApiConstants.ERROR_MESSAGE_INTERNAL_SERVER_ERROR,ApiConstants.REQUEST_TYPE_LOGOUT))

                        }
                    },
                    { throwable ->
                        Log.d("ApiTesting", "onError1 : $throwable")
                        sendErrorResponse(throwable)
                    }
                ))
    }


    fun getParkingMutableLiveData(): MutableLiveData<ApiResponse> {
        return mutableLiveData
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
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

}