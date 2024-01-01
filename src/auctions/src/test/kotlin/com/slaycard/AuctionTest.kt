package com.slaycard

import Auction
import AuctionId
import com.slaycard.basic.plus
import com.slaycard.basic.uuid
import com.slaycard.entities.shared.Money
import com.slaycard.entities.shared.UserId
import com.slaycard.entities.events.AuctionPriceOutbidEvent
import com.slaycard.entities.shared.AuctionItemId
import junit.framework.TestCase.assertFalse
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDateTime
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class AuctionTest {

    private val timeNow = LocalDateTime(2023, 2, 28, 0, 0, 0, 0)

    private var auction = createDefaultAuction()
    @BeforeTest
    fun beforeTest() {
        auction = createDefaultAuction()
    }

    @Test
    fun should_not_be_finished_after_creation() {
        assertFalse(
            auction.isFinished(timeNow + DateTimePeriod(hours = 71)))
    }

    @Test
    fun should_outbid_if_price_greater_by_5_percent() {
        val result = auction.outbid(Money(105), UserId(""), timeNow)

        assertTrue(result.isSuccess)
        assertTrue(auction.events.any {
            val ev = it as? AuctionPriceOutbidEvent ?: return@any false
            ev.auction == auction.id && ev.newPrice == auction.currentPrice
        })
    }

    @Test
    fun should_not_outbid_if_price_not_greater_than_5_percent() {
        val result = auction.outbid(Money(104), UserId(""), timeNow)

        assertFalse(result.isSuccess)
        assertFalse(auction.events.any{ it is AuctionPriceOutbidEvent })
    }

    private fun createDefaultAuction() =
        Auction(
            AuctionId(uuid()),
            UserId("user-1"),
            "Great Sword",
            AuctionItemId("sword-1"),
            quantity = 1,
            startingPrice = Money(100),
            originalDurationHours = 72,
            description = "",
            properties = emptyList())
}
