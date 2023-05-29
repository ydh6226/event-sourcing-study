package com.trading.deposit.repository

import com.trading.deposit.domain.DepositEventSnapshot
import org.springframework.data.jpa.repository.JpaRepository

interface DepositEventSnapshotRepository : JpaRepository<DepositEventSnapshot, Long> {

    fun findByAccountNoOrderByEventIdDesc(accountNo: String): DepositEventSnapshot?
}