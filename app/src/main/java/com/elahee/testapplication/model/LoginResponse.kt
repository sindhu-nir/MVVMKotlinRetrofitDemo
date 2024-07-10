package com.btracsolutions.yesparking.model

data class LoginResponse(
    val data: LoginData,
    val message: String,
    val success: Boolean
)

data class LoginData(
    val tokens: Tokens,
    val userData: UserData,
    var vehicles: ArrayList<Vehicle>
)

data class Tokens(
    val accessToken: String,
    val refreshToken: String
)

data class UserData(
    val _id: String,
    val countryCode: String,
    val mobileNumber: String,
    var name: String,
    var email: String,
    var password: String,
    var nid: String,
    var nidPhoto: String,
    var photo: String,
    val userStatus: String
)

data class Vehicle(
    var _id: String,
    var registrationNumber: String,
    var vehiclePhoto: String,
    var vehicleRegistrationPhoto: String,
    var vehicleType: String,
    var vehicleModel: String
)