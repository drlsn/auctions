package com.slaycard.useCases

import Auction
import AuctionId
import com.slaycard.entities.roots.AuctionRepository
import kotlinx.serialization.Serializable

class AuctionsService(
    private val auctionRepository: AuctionRepository<Auction, AuctionId>
)  {

    fun get(id: String): String {
        return "What up!"
    }

    @Serializable
    data class GetAuctionQueryOut(val id: String, val name: String, val currentPrice: Int)
    fun getAll(): List<GetAuctionQueryOut> =
        auctionRepository.getAll()
            .map{ GetAuctionQueryOut(it.id.value, it.auctionItemName, it.currentPrice.value) }
}
