package com.btracsolutions.yesparking.networking

import androidx.annotation.Keep

/**
 * Created by User on 7/24/2019.
 */
@Keep
class ApiResponse private constructor(
        val status: Status,
        val data: Any?,
        val message: String?,
        val error: Throwable?,
        var requestType: String?
) {

    companion object {
        fun loading(requestType: String?): ApiResponse {
            return ApiResponse(
                    Status.LOADING,
                    null,
                    null,
                    null,
                    requestType
            )
        }

        fun success(
                data: Any,
                message: String?,
                requestType: String?
        ): ApiResponse {
            return ApiResponse(
                    Status.SUCCESS,
                    data,
                    message,
                    null,
                    requestType
            )
        }

        fun apiFailure(
                data: Any?,
                message: String?,
                requestType: String?
        ): ApiResponse {
            return ApiResponse(
                    Status.FAILED,
                    data,
                    message,
                    null,
                    requestType
            )
        }

        fun error(
                error: Throwable?,
                message: String?,
                requestType: String?
        ): ApiResponse {
            return ApiResponse(
                    Status.ERROR,
                    null,
                    message,
                    error,
                    requestType
            )
        }
    }


}