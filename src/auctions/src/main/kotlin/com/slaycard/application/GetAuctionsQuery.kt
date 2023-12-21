package com.slaycard.application

import Auction
import AuctionId
import com.slaycard.basic.domain.Repository
import com.slaycard.basic.ResultT
import com.slaycard.basic.cqrs.QueryHandler
import com.slaycard.basic.getUtcTimeNow
import com.slaycard.basic.resultActionOfT
import kotlinx.serialization.Serializable

class GetAuctionsQueryHandler(
    private val auctionRepository: Repository<Auction, AuctionId>
)
    : QueryHandler<GetAuctionsQuery, GetAuctionsQuery.AuctionsDTO> {

    override fun handle(query: GetAuctionsQuery): ResultT<GetAuctionsQuery.AuctionsDTO> =
        resultActionOfT {
            GetAuctionsQuery.AuctionsDTO(
                auctionRepository.getAll().map {
                    it.toDTO(getUtcTimeNow())
                }
            )
        }
}

@Serializable
class GetAuctionsQuery {
    @Serializable
    data class AuctionsDTO(val auctions: List<GetAuctionQuery.AuctionDTO>)
}
