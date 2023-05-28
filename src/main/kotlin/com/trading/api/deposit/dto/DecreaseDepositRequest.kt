package com.trading.api.deposit.dto

import java.math.BigDecimal
import javax.validation.constraints.NotBlank

data class DecreaseDepositRequest(
    @NotBlank
    val accountNo: String,

    val amount: BigDecimal,
)