package com.trading.deposit.domain

import com.trading.common.BaseTimeEntity
import com.trading.common.JsonUtils
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class DepositEventSnapshot(
    val eventId: String,
    val accountNo: String,
    val payload: String,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    companion object {
        fun of(deposit: Deposit, lastEventId: String): DepositEventSnapshot {
            val payload = JsonUtils.writeValueAsString(deposit)

            return DepositEventSnapshot(
                eventId = lastEventId,
                accountNo = deposit.accountNo,
                payload = payload,
            )
        }
    }
}