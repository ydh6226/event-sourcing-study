package com.trading.deposit.service

import com.trading.common.JsonUtils
import com.trading.deposit.domain.DepositEventEntity
import com.trading.deposit.event.DepositEvent
import com.trading.deposit.repository.DepositEventRepository
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

// TODO: 분산락 잡아야하나? 
// TODO: 스냅샷동시성문제, 낙관적락
// TODO: 쿼리 어떻게 나가는지 구경
@Component
class DepositEventStore(
    private val depositEventRepository: DepositEventRepository,
    private val snapshotStore: DepositEventSnapshotStore,
) {

    private val logger = KotlinLogging.logger {}

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun saveEvent(event: DepositEvent) {
        DepositEventEntity(event.eventType, event.eventId, event.accountNo, JsonUtils.writeValueAsString(event))
            .also { depositEventRepository.save(it) }
    }

    @Async("snapshotTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun createSnapshot(event: DepositEvent) {
        try {
            snapshotStore.store(event)
        } catch (e: Exception) {
            logger.warn(e) { "deposit snapshot 저장 중 에러 발생: ${e.message}" }
        }
    }
}