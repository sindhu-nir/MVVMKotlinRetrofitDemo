package com.btracsolutions.yesparking.repository


import com.btracsolutions.yesparking.networking.RetrofitClient
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import java.util.HashMap

object AuthRepository {

//    fun requestLogin( mobile: String,  password: String  ): Observable<LoginResponse> {
//        val params = HashMap<String, String>()
//        params["mobile"] = mobile
//        params["password"] = password
//
//        return RetrofitClient.apiService.executeLoginRequest(
//            mobile,
//            password
//
//        )
//    }
//    fun requestOtp( mobile: String,  hash: String  ): Observable<LoginWithOtpResponse> {
//        val params = HashMap<String, String>()
//        params["mobile"] = mobile
//        params["hash"] = hash
//
//        return RetrofitClient.apiService.executeOtpRequest(
//            mobile,
//            hash
//
//        )
//    }
//    fun otpVerify( mobile: String,  otp: String  ): Observable<LoginResponse> {
//        val params = HashMap<String, String>()
//        params["mobile"] = mobile
//        params["otp"] = otp
//
//        return RetrofitClient.apiService.executeOtpVerify(
//            mobile,
//            otp
//
//        )
//    }
//
//    fun updateProfile( token:String,id:String,mobile: String,  email: String ): Observable<ProfileUpdateResponse> {
////        val params = HashMap<String, String>()
////        params["token"] = token
////        params["id"] = id
////        params["mobile"] = mobile
////        params["email"] = email
//
//        return RetrofitClient.apiService.executeUpdateProfile(
//            token,
//            mobile,
//            email
//
//        )
//    }
//
////    fun requestECMembers( params:HashMap<String, String>): Observable<MemberListResponse> {
////
////
//////        return RetrofitClient.apiService.executeECMemberList(
//////            params["Authorization"].toString()
//////        )
////    }
//
//    fun getECMembers( params:HashMap<String, String>) : Flow<Response<MemberListResponse>> = flow {
//        val memberListResponse= RetrofitClient.apiService.executeECMemberListFlow(params["Authorization"].toString())
//        emit(memberListResponse)
//    }.flowOn(Dispatchers.IO)

}