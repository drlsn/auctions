package com.slaycard

import Auction
import com.slaycard.entities.Money
import com.slaycard.entities.events.AuctionPriceOutbidEvent
import junit.framework.TestCase.assertFalse
import kotlin.test.Test
import kotlin.test.assertTrue

class AuctionTest {
    @Test
    fun should_outbid_if_price_greater_by_5_percent() {
        val auction = Auction.createDefault()
        val result = auction.outbid(Money(105))

        assertTrue(result.isSuccess)
        assertTrue(auction.events.any{
            (it as AuctionPriceOutbidEvent).let {
                ev -> ev.auction == auction.id && ev.newPrice == auction.currentPrice
            }
        })
    }

    @Test
    fun should_not_outbid_if_price_not_greater_than_5_percent() {
        val auction = Auction.createDefault()
        val result = auction.outbid(Money(104))

        assertFalse(result.isSuccess)
        assertFalse(auction.events.any{ it is AuctionPriceOutbidEvent })
    }

//    @Test
//    fun test() {
//        val time = getUtcTimeNow()
//    }
}
