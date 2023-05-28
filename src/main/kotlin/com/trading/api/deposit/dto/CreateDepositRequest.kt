package com.trading.api.deposit.dto

import java.math.BigDecimal
import javax.validation.constraints.NotBlank

data class CreateDepositRequest(
    @NotBlank
    val accountNo: String,

    val balance: BigDecimal = BigDecimal.ZERO
)