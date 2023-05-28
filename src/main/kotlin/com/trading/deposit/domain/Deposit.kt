package com.trading.deposit.domain

import com.trading.deposit.event.DepositCreatedEvent
import com.trading.deposit.event.DepositDecreasedEvent
import com.trading.deposit.event.DepositIncreasedEvent
import java.math.BigDecimal

data class Deposit(
    val accountNo: String,
    val balance: BigDecimal,
) {

    init {
        require(balance >= BigDecimal.ZERO) { "잔고는 0 이상이어야 합니다. balance: ${balance}" }
    }

    fun on(event: DepositIncreasedEvent): Deposit {
        val afterBalance = balance + event.amount
        return Deposit(accountNo, afterBalance)
    }

    fun on(event: DepositDecreasedEvent): Deposit {
        val afterBalance = balance - event.amount
        return Deposit(accountNo, afterBalance)
    }

    companion object {
        fun from(event: DepositCreatedEvent): Deposit {
            return Deposit(event.accountNo, event.balance)
        }
    }
}