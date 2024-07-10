package com.btracsolutions.yesparking.model

data class OtpSubmitResponse(
    val data: LoginData,
    val message: String,
    val success: Boolean
)
