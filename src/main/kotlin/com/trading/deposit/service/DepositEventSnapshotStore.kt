package com.trading.deposit.service

import com.trading.common.JsonUtils
import com.trading.config.EventConfig
import com.trading.deposit.domain.DepositEventEntity
import com.trading.deposit.domain.DepositEventSnapshot
import com.trading.deposit.event.DepositEvent
import com.trading.deposit.repository.DepositEventRepository
import com.trading.deposit.repository.DepositEventSnapshotRepository
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class DepositEventSnapshotStore(
    private val eventConfig: EventConfig,
    private val depositEventReader: DepositReader,
    private val eventRepository: DepositEventRepository,
    private val snapshotRepository: DepositEventSnapshotRepository,
) {

    private val logger = KotlinLogging.logger {}

    // TODO:  snapshotCount를 늘리거나 줄일경우 문제 없나?
    fun store(event: DepositEvent) {
        val accountNo = event.accountNo

        val lastSnapshot = snapshotRepository.findOrderByEventIdDesc(accountNo)
        val events = findEventEntities(accountNo, lastSnapshot?.eventId)

        if (events.size < eventConfig.snapshotCount) {
            return
        }

        val lastEventId = events.last().eventId
        saveNewSnapshot(accountNo, lastEventId)
    }

    fun findEventEntities(
        accountNo: String,
        gtEventId: String? = null,
        loeEventId: String? = null,
    ): List<DepositEventEntity> {
        return eventRepository.findAllOrderByEventIdAsc(
            accountNo = accountNo,
            gtEventId = gtEventId,
            loeEventId = loeEventId,
            limit = eventConfig.readEventChunkSize
        )
    }

    // events를 조회한 이후 새로운 이벤트가 생기는 경우를 위해 명시적으로 마지막 이벤트ID를 설정
    private fun saveNewSnapshot(
        accountNo: String,
        lastEventId: String,
    ) {
        val deposit = depositEventReader.getDeposit(accountNo, lastEventId)

        val payload = JsonUtils.writeValueAsString(deposit)
        val snapshot = DepositEventSnapshot(lastEventId, accountNo, payload)

        snapshotRepository.save(snapshot)
        logger.info { "[deposit snapshot saved] ${deposit}, eventId: ${lastEventId}" }
    }

}