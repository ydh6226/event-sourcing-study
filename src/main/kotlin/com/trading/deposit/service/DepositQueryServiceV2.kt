package com.trading.deposit.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class DepositQueryServiceV2(
    private val depositReader: DepositReader,
) {
    private val logger = KotlinLogging.logger {}

    fun getCurrentBalance(accountNo: String): BigDecimal {
        logger.info { "예수금 조회: ${accountNo}" }

        val deposit = depositReader.getDeposit(accountNo)

        return deposit.balance
    }
}