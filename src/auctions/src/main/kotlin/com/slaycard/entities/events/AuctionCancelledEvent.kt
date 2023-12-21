package com.slaycard.entities.events

import AuctionId
import PropertyList
import com.slaycard.basic.domain.DomainEvent
import com.slaycard.entities.Money
import com.slaycard.entities.UserId
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class AuctionCancelledEvent(
    val auction: AuctionId,
    val itemName: String,
    val price: Money,
    val cancelTime: LocalDateTime,
) : DomainEvent()
