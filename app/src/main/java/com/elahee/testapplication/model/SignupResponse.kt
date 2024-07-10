package com.btracsolutions.yesparking.model

data class SignupResponse(
    val data: SignUpData,
    val errors: ArrayList<Error>,
    val message: String,
    val next: String,
    val success: Boolean,
    val status: Int,
)

data class SignUpData(
    val _id: String,
    val countryCode: String,
    val mobileNumber: Int,
    val name: String,
    val email: String,
    val userStatus: String
)


data class Error(
    val code: String,
    val expected: String,
    val message: String,
    val path: List<String>,
    val received: String
)