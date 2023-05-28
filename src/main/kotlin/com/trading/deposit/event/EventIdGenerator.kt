package com.trading.deposit.event

import com.fasterxml.uuid.Generators

object EventIdGenerator {

    fun generate(): String {
        val uuid = Generators.timeBasedGenerator().generate()!!
        return uuid.toString()
    }
}