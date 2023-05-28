package com.trading.deposit.service

import com.trading.deposit.domain.Deposit
import com.trading.deposit.event.DepositCreatedEvent
import com.trading.deposit.event.DepositDecreasedEvent
import com.trading.deposit.event.DepositIncreasedEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class DepositEventHandler(
    private val depositReader: DepositReader,
    private val eventPublisher: ApplicationEventPublisher,
) {

    fun on(event: DepositCreatedEvent): Deposit {
        val deposit = Deposit.from(event)
        checkNotExists(event.accountNo)

        eventPublisher.publishEvent(event) // TODO: AOP
        return deposit
    }

    private fun checkNotExists(accountNo: String) {
        require(depositReader.isNotExists(accountNo)){ "${accountNo} 계좌의 잔고가 이미 존재합니다" }
    }

    fun on(event: DepositIncreasedEvent) {
        val deposit = depositReader.getDeposit(event.accountNo)
        deposit.on(event)

        eventPublisher.publishEvent(event)
    }

    fun on(event: DepositDecreasedEvent) {
        val deposit = depositReader.getDeposit(event.accountNo)
        deposit.on(event)

        eventPublisher.publishEvent(event)
    }
}