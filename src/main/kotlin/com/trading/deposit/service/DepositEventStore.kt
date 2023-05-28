package com.trading.deposit.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.trading.deposit.domain.DepositEventEntity
import com.trading.deposit.event.DepositEvent
import com.trading.deposit.repository.DepositEventRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class DepositEventStore(
    private val depositEventRepository: DepositEventRepository,
    private val objectMapper: ObjectMapper
) {

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun on(event: DepositEvent) {
        DepositEventEntity(event.eventType, event.eventId, event.accountNo, objectMapper.writeValueAsString(event))
            .also { depositEventRepository.save(it) }
    }
}