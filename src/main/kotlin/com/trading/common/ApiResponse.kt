package com.trading.common

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?
) {
    companion object {
        fun <T> success(): ApiResponse<T> {
            return ApiResponse(true, null)
        }

        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(true, data)
        }

        fun <T> fail(data: T): ApiResponse<T> {
            return ApiResponse(false, data)
        }
    }
}

