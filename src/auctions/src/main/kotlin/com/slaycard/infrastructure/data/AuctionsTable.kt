package com.slaycard.infrastructure.data

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object AuctionsTable : UUIDTable("auctions") {
    val version                 = integer("version")
    val sellingUserId           = uuid("sellingUserId")
    val auctionItemName         = varchar("auctionItemName", 30)
    val auctionItemId           = uuid("auctionItemId")
    val quantity                = integer("quantity")
    val startingPrice           = integer("startingPrice")
    val currentPrice            = integer("currentPrice")
    val startTime               = datetime("startTime")
    val originalDurationHours   = integer("originalDurationHours")
    val lastBiddingUserId       = uuid("lastBiddingUserId").nullable()
    val cancelTime              = datetime("cancelTime").nullable()
    val description             = varchar("description", 2000)
}
