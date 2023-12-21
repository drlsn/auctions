package com.slaycard.entities.events

import AuctionId
import com.slaycard.basic.DomainEvent
import com.slaycard.entities.Money
import com.slaycard.entities.UserId
import kotlinx.serialization.Serializable

@Serializable
data class AuctionPriceOutbidEvent(
    val auction: AuctionId,
    val byUser: UserId,
    val newPrice: Money
) : DomainEvent()
