package com.trading.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule


object JsonUtils {
    private val mapper: ObjectMapper = ObjectMapper()
        .registerModule(KotlinModule())
        .registerModule(JavaTimeModule())

    fun <T> readValue(payload: String, clazz: Class<T>): T {
        return mapper.readValue(payload, clazz)
    }

    fun writeValueAsString(payload: Any): String {
        return mapper.writeValueAsString(payload)
    }

}