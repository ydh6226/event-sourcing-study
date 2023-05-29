package com.trading.deposit.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.trading.deposit.domain.DepositEventEntity
import com.trading.deposit.domain.QDepositEventEntity.depositEventEntity
import org.springframework.stereotype.Component

@Component
class DepositEventRepository(
    private val query: JPAQueryFactory,
    private val depositEventJpaRepository: DepositEventJpaRepository,
) : DepositEventJpaRepository by depositEventJpaRepository {

    fun findAllByEventIdAsc(accountNo: String, eventId: String?, limit: Long): List<DepositEventEntity> {
        return query.selectFrom(depositEventEntity)
            .where(
                eqAccountNo(accountNo),
                eqEventId(eventId)
            )
            .orderBy(depositEventEntity.eventId.asc())
            .limit(limit)
            .fetch()
    }

    private fun eqAccountNo(accountNo: String): BooleanExpression {
        return depositEventEntity.accountNo.eq(accountNo)
    }

    private fun eqEventId(eventId: String?): BooleanExpression? {
        return eventId?.let { depositEventEntity.eventId.gt(it) }
    }
}