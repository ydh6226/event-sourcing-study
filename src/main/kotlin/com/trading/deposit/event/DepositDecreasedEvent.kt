package com.trading.deposit.event

import com.trading.common.JsonUtils
import com.trading.deposit.domain.DepositEventEntity
import java.math.BigDecimal

class DepositDecreasedEvent(
    val amount: BigDecimal,
    accountNo: String,
    eventId: String = EventIdGenerator.generate(),
) : DepositEvent(accountNo, TYPE, eventId) {

    init {
        require(amount >= BigDecimal.ZERO) { "출금 금액은 0이상이어야 합니다. 요청금액: ${amount}" }
    }

    companion object {
        val TYPE = DepositEventType.DEPOSIT_DECREASED

        fun from(eventEntity: DepositEventEntity): DepositDecreasedEvent {
            check(eventEntity.eventType == TYPE)

            val event = JsonUtils.readValue(eventEntity.payload, DepositDecreasedEvent::class.java)

            return DepositDecreasedEvent(
                accountNo = event.accountNo,
                amount = event.amount,
                eventId = eventEntity.eventId,
            )
        }
    }

}