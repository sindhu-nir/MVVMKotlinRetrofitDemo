package com.btracsolutions.yesparking.model

data class GetParkingResponse(
    val data: ArrayList<ParkingData>,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class ParkingData(
    val _id: String,
    val parkingNo: String,
    val parkingArea: String,
    val address: String,
    val coordinates: ArrayList<Double>,
    val images: ArrayList<String>,
    val ratePerHour: Int,
    var parkingStatus: String,
    val vehicleType: String,
    val status: String,
    val bookedAt: String,
    val rateForFullDay: Int
    )