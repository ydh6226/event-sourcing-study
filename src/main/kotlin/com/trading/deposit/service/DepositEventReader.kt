package com.trading.deposit.service

import com.trading.config.EventConfig
import com.trading.deposit.domain.Deposit
import com.trading.deposit.domain.DepositEventEntity
import com.trading.deposit.event.DepositCreatedEvent
import com.trading.deposit.event.DepositEventType
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

    fun getDeposit(accountNo: String): Deposit {
        var eventEntities = findEventEntities(accountNo)
        require(eventEntities.isNotEmpty()) { "${accountNo} 계좌의 잔고가 없습니다." }

        var deposit: Deposit? = null

        while (true) {
            eventEntities.forEach { deposit = apply(it) }

            if (eventEntities.size < eventConfig.readEventChunkSize) {
                return deposit!!
            } else {
                val lastEventEntityId = eventEntities.last().id
                eventEntities = findEventEntities(accountNo, lastEventEntityId)
            }
        }
    }

    // TODO: reflection
    private fun apply(eventEntity: DepositEventEntity): Deposit {
        logger.info { "[deposit reply] ${eventEntity}}" }

        return when (eventEntity.eventType) {
            DepositEventType.DepositCreated -> Deposit.from(DepositCreatedEvent.from(eventEntity))
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