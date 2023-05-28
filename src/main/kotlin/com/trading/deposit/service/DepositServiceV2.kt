package com.trading.deposit.service

import com.trading.deposit.event.DepositCreatedEvent
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
        checkNotExists(accountNo)

        val event = DepositCreatedEvent(accountNo, balance)
        depositEventHandler.on(event)
    }

    private fun checkNotExists(accountNo: String) {
        require(depositReader.isNotExists(accountNo)){ "${accountNo} 계좌의 잔고가 이미 존재합니다" }
    }
}