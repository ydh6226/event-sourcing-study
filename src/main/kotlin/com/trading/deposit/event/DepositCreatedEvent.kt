package com.trading.deposit.event

import com.trading.common.JsonUtils
import com.trading.deposit.domain.DepositEventEntity
import java.math.BigDecimal

class DepositCreatedEvent(
    val balance: BigDecimal,
    accountNo: String,
    eventId: String = EventIdGenerator.generate(),
) : DepositEvent(accountNo, TYPE, eventId) {

    init {
        require(balance >= BigDecimal.ZERO) { "초기 잔고는 0이상이어야 합니다. 요청금액: ${balance}" }
    }

    companion object {
        val TYPE = DepositEventType.DEPOSIT_CREATED

        fun from(eventEntity: DepositEventEntity): DepositCreatedEvent {
            check(eventEntity.eventType == TYPE)

            val event = JsonUtils.readValue(eventEntity.payload, DepositCreatedEvent::class.java)

            return DepositCreatedEvent(
                accountNo = event.accountNo,
                balance = event.balance,
                eventId = eventEntity.eventId,
            )
        }
    }

}