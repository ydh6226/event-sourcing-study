package com.trading.deposit.repository

import com.trading.deposit.domain.Deposit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DepositRepository: JpaRepository<Deposit, Long> {

    fun findByAccountNo(accountNo: String): Deposit?
}