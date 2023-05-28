package com.trading.deposit.repository

import com.trading.deposit.entity.DepositEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DepositRepository: JpaRepository<DepositEntity, Long> {

    fun findByAccountNo(accountNo: String): DepositEntity?
}