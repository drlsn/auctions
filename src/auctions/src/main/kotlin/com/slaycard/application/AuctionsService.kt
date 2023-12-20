package com.slaycard.application

import Auction
import AuctionId
import AuctionItemId
import Money
import com.slaycard.basic.Repository
import kotlinx.serialization.Serializable
import java.util.UUID



class AuctionsService(
    private val auctionRepository: Repository<Auction, AuctionId>)  {

    fun get(id: String): String {
        return "What up!"
    }

    @Serializable
    data class GetAuctionQueryOut(val id: String, val name: String, val currentPrice: Int)
    fun getAll(): List<GetAuctionQueryOut> =
        auctionRepository.getAll()
            .map{ GetAuctionQueryOut(it.id.value, it.name, it.currentPrice.value) }

    @Serializable
    data class CreateAuctionCommandIn(val name: String, val originalPrice: Int)
    fun add(command: CreateAuctionCommandIn): Boolean {
        if (command.name.isEmpty() || command.originalPrice < 0)
            return false

        return auctionRepository.add(
            Auction(
                AuctionId(UUID.randomUUID().toString()),
                AuctionItemId(UUID.randomUUID().toString()),
                quantity = 1,
                Money(command.originalPrice),
                command.name
            )
        )
    }
}
