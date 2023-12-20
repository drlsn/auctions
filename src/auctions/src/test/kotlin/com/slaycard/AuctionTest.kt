package com.slaycard

import Auction
import AuctionId
import AuctionItemId
import Money
import junit.framework.TestCase.assertFalse
import kotlin.test.Test
import kotlin.test.assertTrue

class AuctionTest {
    @Test
    fun should_outbid_if_price_greater_by_5_percent() {
        val auction = Auction(
            AuctionId("auction-1"),
            name = "Uriziel's Sword",
            AuctionItemId("auction-item-1"),
            quantity = 1,
            startingPrice = Money(100))

        val result = auction.outbid(Money(105))

        assertTrue(result.isSuccess)
    }

    @Test
    fun should_not_outbid_if_price_not_greater_than_5_percent() {
        val auction = Auction(
            AuctionId("auction-1"),
            name = "Uriziel's Sword",
            AuctionItemId("auction-item-1"),
            quantity = 1,
            startingPrice = Money(100))

        val result = auction.outbid(Money(104))

        assertFalse(result.isSuccess)
    }
}
