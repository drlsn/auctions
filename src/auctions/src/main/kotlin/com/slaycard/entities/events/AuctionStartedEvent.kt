package com.slaycard.entities.events

import AuctionId
import PropertyList
import com.slaycard.basic.domain.DomainEvent
import com.slaycard.basic.getUtcTimeNow
import com.slaycard.basic.uuid64
import com.slaycard.entities.Money
import com.slaycard.entities.UserId
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AuctionStartedEvent(
    val auction: AuctionId,
    val byUser: UserId,
    val itemName: String,
    val startingPrice: Money,
    val quantity: Int,
    val description: String,
    val properties: PropertyList,
    val startTime: LocalDateTime,
    val durationHours: Int,
    override val id: String = uuid64(),
    override val utcTimeOccurred: LocalDateTime = getUtcTimeNow()
) : DomainEvent()