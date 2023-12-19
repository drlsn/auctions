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
    fun should_raise_if_greater_price() {
        val auction = Auction(
            AuctionId("auction-1"),
            AuctionItemId("auction-item-1"),
            quantity = 1,
            originalPrice = Money(100),
            name = "Uriziel's Sword")

        val result = auction.raise(Money(200))

        assertTrue(result)
    }

    @Test
    fun should_not_raise_if_same_or_smaller_price() {
        val auction = Auction(
            AuctionId("auction-1"),
            AuctionItemId("auction-item-1"),
            quantity = 1,
            originalPrice = Money(100),
            name = "Uriziel's Sword")

        val result = auction.raise(Money(100))

        assertFalse(result)
    }
}