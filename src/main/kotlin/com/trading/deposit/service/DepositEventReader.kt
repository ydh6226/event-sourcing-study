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
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
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

    fun getDeposit(accountNo: String): Deposit {
        var eventEntities = findEventEntities(accountNo)
        require(eventEntities.isNotEmpty()) { "${accountNo} 계좌의 잔고가 없습니다." }

        while (true) {
            var deposit: Deposit? = null

            eventEntities.forEach { deposit = apply(deposit, it) }

            if (eventEntities.size < eventConfig.readEventChunkSize) {
                logger.info { "deposit: ${deposit}" }
                return deposit!!
            } else {
                val lastEventEntityId = eventEntities.last().id
                eventEntities = findEventEntities(accountNo, lastEventEntityId)
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

    private fun findEventEntities(accountNo: String, lastId: Long = 0): List<DepositEventEntity> {
        return eventRepository.findAllByAccountNoAndIdGreaterThan(accountNo, lastId, getPageable())
    }

    private fun getPageable(): Pageable {
        val sort = Sort.by(Sort.Direction.ASC, "id")
        return PageRequest.of(0, eventConfig.readEventChunkSize, sort)
    }
}