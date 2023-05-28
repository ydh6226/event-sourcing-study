package com.trading.deposit.service

import com.trading.deposit.entity.DepositEntity
import com.trading.deposit.repository.DepositRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class DepositQueryService(
    private val depositRepository: DepositRepository,
) {
    private val logger = KotlinLogging.logger {}

    fun getCurrentBalance(accountNo: String): BigDecimal {
        logger.info { "예수금 조회: ${accountNo}" }

        val deposit = getDeposit(accountNo)
        return deposit.balance
    }

    private fun getDeposit(accountNo: String): DepositEntity {
        return depositRepository.findByAccountNo(accountNo) ?: throw IllegalArgumentException("${accountNo} 계좌의 잔고가 없습니다.")
    }
}