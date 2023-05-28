package com.trading.deposit.service

import com.trading.deposit.domain.Deposit
import com.trading.deposit.event.DepositCreatedEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class DepositEventHandler(
    private val eventPublisher: ApplicationEventPublisher,
) {

    fun on(event: DepositCreatedEvent): Deposit {
        val deposit = Deposit.from(event)

        eventPublisher.publishEvent(event) // TODO: AOP
        return deposit
    }
}