package com.slaycard.entities.events

import AuctionId
import PropertyList
import com.slaycard.basic.domain.DomainEvent
import com.slaycard.entities.Money
import com.slaycard.entities.UserId
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class AuctionStartedEvent(
    val auction: AuctionId,
    val byUser: UserId,
    val itemName: String,
    val startingPrice: Money,
    val quantity: Int,
    val description: String,
    val properties: PropertyList,
    val startTime: LocalDateTime,
    val durationHours: Int
) : DomainEvent()
