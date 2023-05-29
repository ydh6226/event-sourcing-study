package com.trading.deposit.service

import com.trading.config.EventConfig
import com.trading.deposit.domain.Deposit
import com.trading.deposit.domain.DepositEventEntity
import com.trading.deposit.event.DepositCreatedEvent
import com.trading.deposit.event.DepositDecreasedEvent
import com.trading.deposit.event.DepositEventType
import com.trading.deposit.event.DepositIncreasedEvent
import com.trading.deposit.repository.DepositEventRepository
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class DepositReader(
    private val eventConfig: EventConfig,
    private val eventRepository: DepositEventRepository
) {
    private val logger = KotlinLogging.logger {}

    // TODO: 유니크 제약조건 어떻게 걸지??
    fun isNotExists(accountNo: String): Boolean {
        return findEventEntities(accountNo).isEmpty()
    }

    fun getDeposit(accountNo: String, loeEventId: String? = null): Deposit {
        var eventEntities = findEventEntities(accountNo = accountNo, loeEventId = loeEventId)
        require(eventEntities.isNotEmpty()) { "${accountNo} 계좌의 잔고가 없습니다." }

        while (true) {
            var deposit: Deposit? = null

            eventEntities.forEach { deposit = apply(deposit, it) }

            if (eventEntities.size < eventConfig.readEventChunkSize) {
                logger.info { "deposit: ${deposit}" }
                return deposit!!
            } else {
                val lastEventEntityId = eventEntities.last().eventId
                eventEntities = findEventEntities(accountNo, lastEventEntityId, loeEventId)
            }
        }
    }

    // TODO: reflection
    private fun apply(deposit: Deposit?, eventEntity: DepositEventEntity): Deposit {
        val eventType = eventEntity.eventType
        logger.info { "[deposit reply] accountNo: ${eventEntity.accountNo}, type: $eventType, payload: ${eventEntity.payload}" }

        if (eventType != DepositEventType.DEPOSIT_CREATED) {
            checkNotNull(deposit) { "type: $eventType" }
        }

        return when (eventType) {
            DepositEventType.DEPOSIT_CREATED -> Deposit.from(DepositCreatedEvent.from(eventEntity))
            DepositEventType.DEPOSIT_INCREASED -> deposit!!.on(DepositIncreasedEvent.from(eventEntity))
            DepositEventType.DEPOSIT_DECREASED -> deposit!!.on(DepositDecreasedEvent.from(eventEntity))
        }
    }

    private fun findEventEntities(
        accountNo: String,
        gtEventId: String? = null,
        loeEventId: String? = null,
    ): List<DepositEventEntity> {
        return eventRepository.findAllOrderByEventIdAsc(accountNo, gtEventId, loeEventId, eventConfig.readEventChunkSize)
    }
}