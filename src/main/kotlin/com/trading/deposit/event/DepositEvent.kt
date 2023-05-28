package com.trading.deposit.event

import com.fasterxml.jackson.annotation.JsonIgnore

sealed class DepositEvent(
    val accountNo: String,
    @JsonIgnore
    val eventType: DepositEventType,
    @JsonIgnore
    val eventId: String,
)