package com.trading.deposit.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.trading.deposit.domain.DepositEventEntity
import com.trading.deposit.domain.QDepositEventEntity.depositEventEntity
import org.springframework.stereotype.Repository

@Repository
class DepositEventRepository(
    private val query: JPAQueryFactory,
    private val depositEventJpaRepository: DepositEventJpaRepository,
) : DepositEventJpaRepository by depositEventJpaRepository {

    // gtEventId < eventId <= loeEventId
    fun findAllOrderByEventIdAsc(
        accountNo: String,
        gtEventId: String? = null,
        loeEventId: String? = null,
        limit: Long,
    ): List<DepositEventEntity> {
        if (gtEventId != null && loeEventId != null) {
            check(gtEventId <= loeEventId) { "gtEventId[${gtEventId}] <= loeEventId${loeEventId} 를 만족해야합니다." }
        }

        return query.selectFrom(depositEventEntity)
            .where(
                eqAccountNo(accountNo),
                gtEventId(gtEventId),
                loeEventId(loeEventId)
            )
            .orderBy(depositEventEntity.eventId.asc())
            .limit(limit)
            .fetch()
    }

    private fun eqAccountNo(accountNo: String): BooleanExpression {
        return depositEventEntity.accountNo.eq(accountNo)
    }

    private fun gtEventId(gtEventId: String?): BooleanExpression? {
        return gtEventId?.let { depositEventEntity.eventId.gt(it) }
    }

    private fun loeEventId(loeEventId: String?): BooleanExpression? {
        return loeEventId?.let { depositEventEntity.eventId.loe(it) }
    }
}