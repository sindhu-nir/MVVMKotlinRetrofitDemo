package com.btracsolutions.yesparking.networking


import com.btracsolutions.yesparking.utils.ApiConstants
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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    //    @GET("get_geofences")
//    fun executeGetGeofenceRequest(
//        @Query("lang") lang: String,
//        @Query("user_api_hash") userApiHash: String
//    ): Observable<Object>
//
    @POST(ApiConstants.LOGIN)
    fun executeLoginRequest(
        @Body loginApiload: HashMap<String, String>
    ): Observable<LoginResponse>

    @POST(ApiConstants.LOGOUT)
    fun executeLogoutRequest(
        @Header("Authorization") token: String,
        @Body loginApiload: HashMap<String, String>
    ): Observable<CommonResponse>


    @POST(ApiConstants.SIGNUP)
    fun executeSignupRequest(
        @Body signApiPayload: HashMap<String, String>
    ): Observable<SignupResponse>

    @POST(ApiConstants.OTP_VERIFY)
    fun executeSubmitOTPRequest(
        @Body apiPayload: HashMap<String, String>
    ): Observable<OtpSubmitResponse>

    @POST(ApiConstants.OTP_RESEND)
    fun executeResendOTPRequest(
        @Body apiPayload: HashMap<String, String>
    ): Observable<CommonResponse>

    @POST(ApiConstants.REFRESH_TOKEN)
    suspend fun executeTokenRefreshRequest(
        @Body apiPayload: HashMap<String, String>
    ): Response<RefreshTokenResponse>

    @POST(ApiConstants.FORGOT_PASS_SEND_OTP)
    fun executeForgotPassOTPRequest(
        @Body apiPayload: HashMap<String, String>
    ): Observable<ForgotPassOtpResponse>
    @POST(ApiConstants.FORGOT_PASS_SUBMIT_OTP)
    fun executeForgotPassSubmitOTPRequest(
        @Body apiPayload: HashMap<String, String>
    ): Observable<CommonResponse>
    @POST(ApiConstants.FORGOT_PASS_RESEND_OTP)
    fun executeForgotPassReSendOTPRequest(
        @Body apiPayload: HashMap<String, String>
    ): Observable<CommonResponse>
    @PUT(ApiConstants.FORGOT_PASS)
    fun executeForgotPassSETRequest(
        @Body apiPayload: HashMap<String, String>
    ): Observable<CommonResponse>

    @Multipart
    @POST(ApiConstants.UPLOAD_FILE)
    suspend fun executeFileUploadRequest(
        @Header("Authorization") token: String,
        @Query("type") type: String,
        @Part file: MultipartBody.Part
    ): Response<FileUploadResponse>

    @POST(ApiConstants.ADD_VEHICLE)
    suspend fun executeAddVehicleRequest(
        @Header("Authorization") token: String,
        @Body apiPayload: HashMap<String, String>
    ): Response<AddVehicleResponse>

    @PUT(ApiConstants.ADD_VEHICLE + "/{vehicleid}")
    suspend fun executeEditVehicleRequest(
        @Header("Authorization") token: String,
        @Body apiPayload: HashMap<String, String>,
        @Path("vehicleid") vehicleid: String
    ): Response<AddVehicleResponse>

    @GET(ApiConstants.PARKING_SPACES)
    suspend fun executeParkingDataRequest(
        @Header("Authorization") token: String
    ): Response<GetParkingResponse>

    //    @POST(ApiConstants.UPDATE_USER+"{userId}")
//    suspend fun executeUserNIDUpdateRequest(
//        @Header("Authorization") token: String,
//        @Field("nid") type: String,
//        @Part nidPhoto: MultipartBody.Part,
//        @Path("userId") bookingId: String,
//    ): Response<UserUpdateResponse>
    @POST(ApiConstants.UPDATE_USER + "{userId}")
    suspend fun executeUserNIDUpdateRequest(
        @Header("Authorization") token: String,
        @Path("userId") bookingId: String,
        @Body body: RequestBody
    ): Response<UserUpdateResponse>

    @DELETE(ApiConstants.UPDATE_USER + "{userId}")
    suspend fun executeDeleteUserRequest(
        @Header("Authorization") token: String,
        @Path("userId") bookingId: String
    ): Response<CommonResponse>

    @Multipart
    @PUT(ApiConstants.UPDATE_USER + "{userId}")
    suspend fun executeProfilePicUploadRequest(
        @Header("Authorization") token: String,
        @Path("userId") bookingId: String,
        @Part file: MultipartBody.Part
    ): Response<UserUpdateResponse>

    @POST(ApiConstants.BOOK_PARKING)
    suspend fun executeParkingBookRequest(
        @Header("Authorization") token: String,
        @Body apiPayload: HashMap<String, Any>
    ): Response<BookedResponse>

    @PUT(ApiConstants.CHECK_IN_OUT + "{bookingid}")
    suspend fun executeExtendRequest(
        @Header("Authorization") token: String,
        @Body apiPayload: HashMap<String, Any>,
        @Path("bookingid") bookingId: String

    ): Response<CheckInResponse>

    @PUT(ApiConstants.CHECK_IN_OUT + "{bookingid}")
    suspend fun executeCheckInOutRequest(
        @Header("Authorization") token: String,
        @Body apiPayload: HashMap<String, Any>,
        @Path("bookingid") bookingId: String
    ): Response<CheckInResponse>

    @PUT(ApiConstants.CHECK_IN_OUT + "{bookingid}")
    suspend fun executeCancelBookingRequest(
        @Header("Authorization") token: String,
        @Body apiPayload: HashMap<String, Any>,
        @Path("bookingid") bookingId: String
    ): Response<CheckInResponse>

    @GET(ApiConstants.PROMO + "{promocode}")
    suspend fun executeCheckPromoRequest(
        @Header("Authorization") token: String,
        @Path("promocode") promocode: String
    ): Response<PromoResponse>

    @GET(ApiConstants.DATASYNC + "{userid}")
    suspend fun executeDataSyncRequest(
        @Header("Authorization") token: String,
        @Path("userid") userid: String
    ): Response<SyncResponse>

    @PUT(ApiConstants.CHECK_IN_OUT + "{bookingid}")
    suspend fun executePaymentRequest(
        @Header("Authorization") token: String,
        @Body apiPayload: HashMap<String, Any>,
        @Path("bookingid") bookingId: String
    ): Response<ConfirmPaymentResponse>

    @POST(ApiConstants.PAYMENTS )
    suspend fun executePaymentRequestNew(
        @Header("Authorization") token: String,
        @Body apiPayload: HashMap<String, Any>,
    ): Response<ConfirmPaymentResponse>

    @PUT(ApiConstants.CHECK_IN_OUT + "{bookingid}")
    suspend fun executeRatingRequest(
        @Header("Authorization") token: String,
        @Body apiPayload: HashMap<String, Any>,
        @Path("bookingid") bookingId: String

    ): Response<CommonResponse>

    //    @PUT(ApiConstants.UPDATE_USER+"{userid}")
//    suspend fun executeUpdateUserInfoRequest(
//        @Header("Authorization") token: String,
//        @Path("userid") userid: String,
//        @Query("name") name: String,
//        @Query("email") email: String,
//        @Query("password") password: String
//
//    ): Response<UserUpdateResponse>
    @PUT(ApiConstants.UPDATE_USER + "{userid}")
    suspend fun executeUpdateUserInfoRequest(
        @Header("Authorization") token: String,
        @Path("userid") userid: String,
        @Body body: RequestBody
    ): Response<UserUpdateResponse>

    @GET(ApiConstants.BOOK_PARKING)
    suspend fun executeHistoryRequest(
        @Header("Authorization") token: String,
        @Query("user") user: String,
        @Query("filterType") filterType: String
    ): Response<HistoryResponse>

    @GET(ApiConstants.CHECK_IN_OUT + "{bookingid}")
    suspend fun executeCheckPaymentRequest(
        @Header("Authorization") token: String,
        @Path("bookingid") bookingId: String
    ): Response<CheckInResponse>

}

