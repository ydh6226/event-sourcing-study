package com.trading.deposit.service

import com.trading.deposit.entity.DepositEntity
import com.trading.deposit.repository.DepositRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Transactional
@Service
class DepositService(
    private val depositRepository: DepositRepository,
) {

    private val logger = KotlinLogging.logger {}

    fun create(accountNo: String, balance: BigDecimal) {
        logger.info { "잔고생성: accountNo: ${accountNo}, balance: ${balance}" }
        checkNotExists(accountNo)

        val deposit = DepositEntity(accountNo, balance)
        depositRepository.save(deposit)
    }

    private fun checkNotExists(accountNo: String) {
        depositRepository.findByAccountNo(accountNo)?.let { throw IllegalArgumentException("${accountNo} 계좌의 잔고가 이미 존재합니다") }
    }

    fun increase(accountNo: String, amount: BigDecimal) {
        logger.info { "입금 요청: accountNo: ${accountNo}, amount: ${amount}" }

        val deposit = getDeposit(accountNo)
        deposit.increase(amount)
    }

    fun decrease(accountNo: String, amount: BigDecimal) {
        logger.info { "출금 요청: accountNo: ${accountNo}, amount: ${amount}" }

        val deposit = getDeposit(accountNo)
        deposit.decrease(amount)
    }

    private fun getDeposit(accountNo: String): DepositEntity {
        return depositRepository.findByAccountNo(accountNo) ?: throw IllegalArgumentException("${accountNo} 계좌의 잔고가 없습니다.")
    }
}