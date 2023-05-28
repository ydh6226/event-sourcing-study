package com.trading.deposit.domain

import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Deposit(
    val accountNo: String,
    var balance: BigDecimal,
) {
    init {
        require(balance >= BigDecimal.ZERO) { "예수금은 0보다 커야합니다." }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    fun increase(amount: BigDecimal) {
        balance += amount
    }

    fun decrease(amount: BigDecimal) {
        val tempBalance = balance - amount
        require(tempBalance >= BigDecimal.ZERO) { "예수금이 부족합니다. 보유: ${balance}, 출금요청금액: ${amount}" }

        balance = tempBalance
    }
}
