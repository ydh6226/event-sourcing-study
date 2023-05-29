package com.trading.deposit.event

import org.apache.commons.lang3.RandomStringUtils
import java.time.Instant

object EventIdGenerator {

    fun generate(): String {
//        val uuid = Generators.timeBasedGenerator().generate()!!
//        return uuid.toString()

        val hexNanoSeconds = getCurrentHexNanoSeconds()
        val randomAlphabetic = RandomStringUtils.randomAlphabetic(10)
        return hexNanoSeconds + randomAlphabetic
    }

    private fun getCurrentHexNanoSeconds(): String {
        val instant = Instant.now()
        val milli = instant.toEpochMilli().toString()
        val nano = String.format("%09d", instant.nano).substring(3)
        return (milli + nano).toLong().toString(16)
    }
}