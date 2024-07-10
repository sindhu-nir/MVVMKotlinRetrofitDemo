package com.btracsolutions.yesparking.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PushNotificationData(
    @SerializedName("body")
    val body: String?,
    @SerializedName("refId")
    val refId: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("type")
    val type: String?,

)