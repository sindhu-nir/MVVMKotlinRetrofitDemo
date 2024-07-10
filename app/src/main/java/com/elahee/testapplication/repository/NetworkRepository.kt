package com.btracsolutions.yesparking.repository

import com.btracsolutions.yesparking.networking.ApiService
import com.btracsolutions.yesparking.model.AddVehicleResponse
import com.btracsolutions.yesparking.model.BookedResponse
import com.btracsolutions.yesparking.model.CheckInResponse
import com.btracsolutions.yesparking.model.CommonResponse
import com.btracsolutions.yesparking.model.ConfirmPaymentResponse
import com.btracsolutions.yesparking.model.FileUploadResponse
import com.btracsolutions.yesparking.model.ForgotPassOtpResponse
import com.btracsolutions.yesparking.model.GetParkingResponse
import com.btracsolutions.yesparking.model.HistoryResponse
import com.btracsolutions.yesparking.model.LoginResponse
import com.btracsolutions.yesparking.model.OtpSubmitResponse
import com.btracsolutions.yesparking.model.PromoResponse
import com.btracsolutions.yesparking.model.RefreshTokenResponse
import com.btracsolutions.yesparking.model.SignupResponse
import com.btracsolutions.yesparking.model.SyncResponse
import com.btracsolutions.yesparking.model.UserUpdateResponse
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.util.HashMap
import javax.inject.Inject

class NetworkRepository @Inject constructor(val apiService: ApiService) {

    fun requestLogin(hashMap: HashMap<String, String>): Observable<LoginResponse> {
        return apiService.executeLoginRequest(
            hashMap

        )
    }
    fun requestLogOut(token:String,hashMap: HashMap<String, String>): Observable<CommonResponse> {
        return apiService.executeLogoutRequest(
            token,
            hashMap

        )
    }

    fun requestSignup(hashMap: HashMap<String, String>): Observable<SignupResponse> {

        return apiService.executeSignupRequest(
            hashMap
        )
    }


    fun requestSubmitOTP(hashMap: HashMap<String, String>): Observable<OtpSubmitResponse> {
        return apiService.executeSubmitOTPRequest(
            hashMap

        )
    }

    fun requestResendOTP(hashMap: HashMap<String, String>): Observable<CommonResponse> {
        return apiService.executeResendOTPRequest(
            hashMap

        )
    }
    fun requestForgotPassOTP(hashMap: HashMap<String, String>): Observable<ForgotPassOtpResponse> {
        return apiService.executeForgotPassOTPRequest(
            hashMap
        )
    }
 fun requestForgotPassSubmitOTP(hashMap: HashMap<String, String>): Observable<CommonResponse> {
        return apiService.executeForgotPassSubmitOTPRequest(
            hashMap
        )
    }
    fun requestForgotPassReSendOTP(hashMap: HashMap<String, String>): Observable<CommonResponse> {
        return apiService.executeForgotPassReSendOTPRequest(
            hashMap
        )
    }
    fun requestForgotPassSETRequest(hashMap: HashMap<String, String>): Observable<CommonResponse> {
        return apiService.executeForgotPassSETRequest(
            hashMap
        )
    }

    fun requestAddVehicle(
        authorization: String,
        hashMap: HashMap<String, String>
    ): Flow<Response<AddVehicleResponse>> = flow {
        val response = apiService.executeAddVehicleRequest(
            authorization,
            hashMap
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun requestEditVehicle(
        authorization: String,
        hashMap: HashMap<String, String>,vehicleid:String
    ): Flow<Response<AddVehicleResponse>> = flow {
        val response = apiService.executeEditVehicleRequest(
            authorization,
            hashMap,
            vehicleid
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun refreshToken(
        hashMap: HashMap<String, String>
    ): Flow<Response<RefreshTokenResponse>> = flow {
        val response = apiService.executeTokenRefreshRequest(
            hashMap
        )
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun uploadFile(
        authorization: String,
        type: String,
        file: MultipartBody.Part
    ): Flow<Response<FileUploadResponse>> = flow {
        val durationResponse = apiService.executeFileUploadRequest(
            authorization,
            type,
            file
        )
        emit(durationResponse)
    }.flowOn(Dispatchers.IO)


    fun requestParkingData(
        authorization: String
    ): Flow<Response<GetParkingResponse>> = flow {
        val response = apiService.executeParkingDataRequest(
            authorization
        )
        emit(response)
    }.flowOn(Dispatchers.IO)


    fun uploadNID(
        authorization: String,
        nid: String,
        nidPhoto: MultipartBody.Part,
        userid: String,
        requestBody: RequestBody
    ): Flow<Response<UserUpdateResponse>> = flow {
        val durationResponse = apiService.executeUserNIDUpdateRequest(
            authorization,
            userid,
            requestBody
        )
        emit(durationResponse)
    }.flowOn(Dispatchers.IO)

    fun deleteUser(
        authorization: String,
        userid: String
    ): Flow<Response<CommonResponse>> = flow {
        val response = apiService.executeDeleteUserRequest(
            authorization,
            userid
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun uploadProfilePic(
        authorization: String,
        userid: String,
        file: MultipartBody.Part
    ): Flow<Response<UserUpdateResponse>> = flow {
        val durationResponse = apiService.executeProfilePicUploadRequest(
            authorization,
            userid,
            file
        )
        emit(durationResponse)
    }.flowOn(Dispatchers.IO)

    fun bookParking(auth:String,
        hashMap: HashMap<String, Any>
    ): Flow<Response<BookedResponse>> = flow {
        val response = apiService.executeParkingBookRequest(
            auth,
            hashMap
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun checkInOUTParking(auth:String,
                    hashMap: HashMap<String, Any>,bookingId: String
    ): Flow<Response<CheckInResponse>> = flow {
        val response = apiService.executeCheckInOutRequest(
            auth,
            hashMap,
            bookingId
        )
        emit(response)
    }.flowOn(Dispatchers.IO)
    fun cancelBooking(auth:String,
                    hashMap: HashMap<String, Any>,bookingId: String
    ): Flow<Response<CheckInResponse>> = flow {
        val response = apiService.executeCancelBookingRequest(
            auth,
            hashMap,
            bookingId
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun extendParking(auth:String,hashMap: HashMap<String, Any>,bookingId: String
    ): Flow<Response<CheckInResponse>> = flow {
        val response = apiService.executeExtendRequest(
            auth,
            hashMap,
            bookingId
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun paymentReq(auth:String,hashMap: HashMap<String, Any>
    ): Flow<Response<ConfirmPaymentResponse>> = flow {
        val response = apiService.executePaymentRequestNew(
            auth,
            hashMap
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun checkInOUTParking(auth:String,promocode: String
    ): Flow<Response<PromoResponse>> = flow {
        val response = apiService.executeCheckPromoRequest(
            auth,
            promocode
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun syncParkingData(auth:String,userid: String
    ): Flow<Response<SyncResponse>> = flow {
        val response = apiService.executeDataSyncRequest(
            auth,
            userid
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun sendRating(auth:String,
                          hashMap: HashMap<String, Any>,bookingId: String
    ): Flow<Response<CommonResponse>> = flow {
        val response = apiService.executeRatingRequest(
            auth,
            hashMap,
            bookingId
        )
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun updateUserInfo(
        authorization: String,
        userid: String,
        name: String,
        email: String,
        password: String,
        requestBody: RequestBody

    ): Flow<Response<UserUpdateResponse>> = flow {
        val durationResponse = apiService.executeUpdateUserInfoRequest(
            authorization,
            userid,
            requestBody
          //  name,email,password
        )
        emit(durationResponse)
    }.flowOn(Dispatchers.IO)

    fun getHistory(
        authorization: String,
        userid: String,
        filterType: String
        ): Flow<Response<HistoryResponse>> = flow {
        val durationResponse = apiService.executeHistoryRequest(
            authorization,
            userid,
            filterType
        )
        emit(durationResponse)
    }.flowOn(Dispatchers.IO)

    fun checkPayment(
        authorization: String,
        bookingId: String,
    ): Flow<Response<CheckInResponse>> = flow {
        val durationResponse = apiService.executeCheckPaymentRequest(
            authorization,
            bookingId
        )
        emit(durationResponse)
    }.flowOn(Dispatchers.IO)


}