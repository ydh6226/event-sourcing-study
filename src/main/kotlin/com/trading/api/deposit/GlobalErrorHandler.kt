package com.trading.api.deposit

import com.trading.common.ApiResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalErrorHandler {

    private val logger = KotlinLogging.logger {}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    fun on(e: IllegalArgumentException): ApiResponse<String> {
        logger.warn(e) { "HTTP STATUS 400 [${e.message}]" }
        return ApiResponse.fail(e.message ?: "")
    }
}