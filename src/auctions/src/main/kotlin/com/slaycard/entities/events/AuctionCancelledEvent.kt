package com.slaycard.entities.events

import AuctionId
import com.slaycard.basic.domain.DomainEvent
import com.slaycard.basic.getUtcTimeNow
import com.slaycard.basic.uuid
import com.slaycard.entities.shared.Money
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class AuctionCancelledEvent(
    val auction: AuctionId,
    val itemName: String,
    val price: Money,
    val cancelTime: LocalDateTime,
    override val id: String = uuid(),
    override val utcTimeOccurred: LocalDateTime = getUtcTimeNow()
) : DomainEvent()
