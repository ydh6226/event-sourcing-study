package com.trading.deposit.domain

import com.trading.common.BaseTimeEntity
import com.trading.deposit.event.DepositEventType
import javax.persistence.*

@Entity
@Table(name = "deposit_event")
class DepositEventEntity(
    @Enumerated(EnumType.STRING)
    val eventType: DepositEventType,
    val eventId: String,
    val accountNo: String,
    val payload: String,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
}