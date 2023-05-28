package com.trading.deposit.event

import com.trading.common.JsonUtils
import com.trading.deposit.domain.DepositEventEntity
import java.math.BigDecimal

class DepositCreatedEvent(
    val accountNo: String,
    val balance: BigDecimal,
    eventId: String = EventIdGenerator.generate(),
) : DepositEvent(TYPE, eventId) {

    companion object {
        val TYPE = DepositEventType.DepositCreated

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