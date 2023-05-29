package com.trading.deposit.repository

import com.trading.deposit.domain.DepositEventSnapshot
import org.springframework.data.jpa.repository.JpaRepository

interface DepositEventSnapshotJpaRepository : JpaRepository<DepositEventSnapshot, Long> {
}