package com.slaycard.entities.events

import AuctionId
import PropertyList
import com.slaycard.basic.domain.DomainEvent
import com.slaycard.basic.getUtcTimeNow
import com.slaycard.basic.uuid
import com.slaycard.entities.shared.Money
import com.slaycard.entities.shared.UserId
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class AuctionStartedEvent(
    val auction: AuctionId,
    val byUser: UserId,
    val itemName: String,
    val startingPrice: Money,
    val quantity: Int,
    val description: String,
    val startTime: LocalDateTime,
    val durationHours: Int,
    override val id: String = uuid(),
    override val utcTimeOccurred: LocalDateTime = getUtcTimeNow()
) : DomainEvent()