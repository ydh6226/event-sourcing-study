package com.trading.deposit.event

import com.fasterxml.jackson.annotation.JsonIgnore

sealed class DepositEvent(
    @JsonIgnore
    val eventType: DepositEventType,
    @JsonIgnore
    val eventId: String,
)