package com.trading.deposit.domain

import com.trading.deposit.event.DepositCreatedEvent
import java.math.BigDecimal

class Deposit(
    val accountNo: String,
    var balance: BigDecimal,
) {

    companion object {
        fun from(event: DepositCreatedEvent): Deposit {
            return Deposit(event.accountNo, event.balance)
        }
    }
}