package com.trading.deposit.repository

import com.trading.deposit.domain.DepositEventEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DepositEventJpaRepository : JpaRepository<DepositEventEntity, Long> {
}