package com.slaycard.application

import Auction
import AuctionId
import com.slaycard.basic.Repository

class AuctionsService(
    val auctionRepository: Repository<Auction, AuctionId>)  {

    fun get(id: String): String {
        return "What up!"
    }
}
