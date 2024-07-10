package com.btracsolutions.yesparking.model

data class ConfirmPaymentResponse(
    val data: PaymentData,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class PaymentData(
    val redirect: String,
    val redirectGatewayURL: String,
    val booking: CheckInData
)