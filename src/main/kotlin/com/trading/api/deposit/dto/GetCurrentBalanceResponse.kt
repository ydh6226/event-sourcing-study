package com.trading.api.deposit.dto

import java.math.BigDecimal

data class GetCurrentBalanceResponse(
    val balance: BigDecimal,
)

