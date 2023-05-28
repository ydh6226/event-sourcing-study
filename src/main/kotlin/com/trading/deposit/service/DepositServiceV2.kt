package com.trading.deposit.service

import com.trading.deposit.event.DepositCreatedEvent
import com.trading.deposit.event.DepositDecreasedEvent
import com.trading.deposit.event.DepositIncreasedEvent
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Transactional
@Service
class DepositServiceV2(
    private val depositEventHandler: DepositEventHandler,
    private val depositReader: DepositReader,
) {

    private val logger = KotlinLogging.logger {}

    fun create(accountNo: String, balance: BigDecimal) {
        logger.info { "잔고생성: accountNo: ${accountNo}, balance: ${balance}" }

        val event = DepositCreatedEvent(balance, accountNo)
        depositEventHandler.on(event)
    }

    fun increase(accountNo: String, amount: BigDecimal) {
        logger.info { "입금 요청: accountNo: ${accountNo}, amount: ${amount}" }

        val event = DepositIncreasedEvent(amount, accountNo)
        depositEventHandler.on(event)
    }

    fun decrease(accountNo: String, amount: BigDecimal) {
        logger.info { "출금 요청: accountNo: ${accountNo}, amount: ${amount}" }

        val event = DepositDecreasedEvent(amount, accountNo)
        depositEventHandler.on(event)
    }
}