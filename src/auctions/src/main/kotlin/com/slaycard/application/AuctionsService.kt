package com.slaycard.application

import Auction
import AuctionId
import AuctionItemId
import Money
import com.slaycard.basic.Repository
import java.util.UUID

class AuctionsService(
    private val auctionRepository: Repository<Auction, AuctionId>)  {

    fun get(id: String): String {
        return "What up!"
    }

    fun getAll(): String =
        auctionRepository.getAll()
            .map{ "Id: ${it.id.value}\nName: ${it.name}\nCurrent Price: ${it.currentPrice.value}" }
            .reduce{ x, y -> x + y }

    fun add(name: String, originalPrice: Int): Boolean =
        auctionRepository.add(
            Auction(
                AuctionId(UUID.randomUUID().toString()),
                AuctionItemId(UUID.randomUUID().toString()),
                quantity = 1,
                Money(originalPrice),
                name))
}
