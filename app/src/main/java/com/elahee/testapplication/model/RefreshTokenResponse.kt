package com.btracsolutions.yesparking.model

data class RefreshTokenResponse(
    val data: RefreshData,
    val message: String,
    val next: String,
    val success: Boolean
)

data class RefreshData(
    val accessToken: String
)