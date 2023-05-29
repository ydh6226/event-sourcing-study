package com.trading.deposit.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.trading.deposit.domain.DepositEventSnapshot
import com.trading.deposit.domain.QDepositEventSnapshot.depositEventSnapshot
import org.springframework.stereotype.Repository

@Repository
class DepositEventSnapshotRepository(
    private val query: JPAQueryFactory,
    private val depositEventSnapshotJpaRepository: DepositEventSnapshotJpaRepository,
) : DepositEventSnapshotJpaRepository by depositEventSnapshotJpaRepository {

    fun findOrderByEventIdDesc(
        accountNo: String,
        loeEventId: String? = null,
    ): DepositEventSnapshot? {
        return query.selectFrom(depositEventSnapshot)
            .where(
                eqAccountNo(accountNo),
                loeEventId(loeEventId)
            )
            .orderBy(depositEventSnapshot.eventId.desc())
            .limit(1)
            .fetchOne()
    }

    private fun eqAccountNo(accountNo: String): BooleanExpression {
        return depositEventSnapshot.accountNo.eq(accountNo)
    }

    private fun loeEventId(loeEventId: String?): BooleanExpression? {
        return loeEventId?.let { depositEventSnapshot.eventId.loe(it) }
    }
}