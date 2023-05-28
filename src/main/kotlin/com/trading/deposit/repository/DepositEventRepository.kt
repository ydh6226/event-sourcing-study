package com.trading.deposit.repository

import com.trading.deposit.domain.DepositEventEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DepositEventRepository: JpaRepository<DepositEventEntity, Long> {

    // TODO: queryDsl사용해서 'eventId > ' 조건으로 변경. eventId는 nullable하게.
    fun findAllByAccountNoAndIdGreaterThan(accountNo: String, id: Long, pageable: Pageable): List<DepositEventEntity>
}