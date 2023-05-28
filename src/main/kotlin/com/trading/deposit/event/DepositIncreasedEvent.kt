package com.trading.deposit.event

import com.trading.common.JsonUtils
import com.trading.deposit.domain.DepositEventEntity
import java.math.BigDecimal

class DepositIncreasedEvent(
    val amount: BigDecimal,
    accountNo: String,
    eventId: String = EventIdGenerator.generate(),
) : DepositEvent(accountNo, TYPE, eventId) {

    init {
        require(amount >= BigDecimal.ZERO) { "입금 금액은 0이상이어야 합니다. 요청금액: ${amount}" }
    }

    companion object {
        val TYPE = DepositEventType.DEPOSIT_INCREASED

        fun from(eventEntity: DepositEventEntity): DepositIncreasedEvent {
            check(eventEntity.eventType == TYPE)

            val event = JsonUtils.readValue(eventEntity.payload, DepositIncreasedEvent::class.java)

            return DepositIncreasedEvent(
                accountNo = event.accountNo,
                amount = event.amount,
                eventId = eventEntity.eventId,
            )
        }
    }

}