package com.slaycard.entities.events

import AuctionId
import com.slaycard.basic.domain.DomainEvent
import com.slaycard.basic.getUtcTimeNow
import com.slaycard.basic.uuid64
import com.slaycard.entities.shared.Money
import com.slaycard.entities.shared.UserId
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class AuctionPriceOutbidEvent(
    val auction: AuctionId,
    val itemName: String,
    val byUser: UserId,
    val newPrice: Money,
    override val id: String = uuid64(),
    override val utcTimeOccurred: LocalDateTime = getUtcTimeNow()
) : DomainEvent()
