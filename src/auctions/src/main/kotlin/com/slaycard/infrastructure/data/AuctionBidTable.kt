package com.slaycard.infrastructure.data

import org.jetbrains.exposed.dao.id.UUIDTable

object AuctionBidTable : UUIDTable("auctionBids") {
    val originalPrice = integer("originalPrice")
}
