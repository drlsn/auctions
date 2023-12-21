package com.slaycard.entities.events

import AuctionId
import PropertyList
import com.slaycard.basic.domain.DomainEvent
import com.slaycard.basic.getUtcTimeNow
import com.slaycard.basic.uuid64
import com.slaycard.entities.Money
import com.slaycard.entities.UserId
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class AuctionCancelledEvent(
    val auction: AuctionId,
    val itemName: String,
    val price: Money,
    val cancelTime: LocalDateTime,
    override val id: String = uuid64(),
    override val utcTimeOccurred: LocalDateTime = getUtcTimeNow()
) : DomainEvent()
