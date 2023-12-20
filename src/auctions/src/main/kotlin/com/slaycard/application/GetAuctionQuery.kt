package com.slaycard.application

import Auction
import AuctionId
import com.slaycard.basic.*
import com.slaycard.basic.cqrs.QueryHandler
import kotlinx.serialization.Serializable

class GetAuctionQueryHandler(
    private val auctionRepository: Repository<Auction, AuctionId>)
    : QueryHandler<GetAuctionQuery, GetAuctionQuery.AuctionDTO> {

    override fun handle(query: GetAuctionQuery): ResultT<GetAuctionQuery.AuctionDTO> =
        resultActionOfT {
            if (query.auctionId.isEmpty())
                it.fail("The id must be of proper format")

            val auction = auctionRepository.get(AuctionId(query.auctionId))
            if (auction == null) {
                it.fail("The auction does not exist");
                return@resultActionOfT null
            }

            GetAuctionQuery.AuctionDTO(auction.id.value, auction.name, auction.startingPrice.value)
        }

}

@Serializable
data class GetAuctionQuery(val auctionId: String) {

    @Serializable
    data class AuctionDTO(
        val id: String,
        val name: String,
        val originalPrice: Int)

}
