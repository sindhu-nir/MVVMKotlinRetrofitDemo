package com.btracsolutions.yesparking.model

data class ForgotPassOtpResponse(
    val data: ForgotPassOtpData,
    val message: String,
    val next: String,
    val success: Boolean


)

data class ForgotPassOtpData(
    val userId: String?=null
)