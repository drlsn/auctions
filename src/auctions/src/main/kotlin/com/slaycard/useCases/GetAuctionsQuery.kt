package com.slaycard.useCases

import Auction
import AuctionId
import com.slaycard.entities.roots.AuctionRepository
import com.slaycard.basic.ResultT
import com.slaycard.basic.cqrs.QueryHandler
import com.slaycard.basic.getUtcTimeNow
import com.slaycard.basic.resultActionOfT
import com.slaycard.basic.suspendedResultActionOfT
import kotlinx.coroutines.Deferred
import kotlinx.serialization.Serializable

class GetAuctionsQueryHandler(
    private val auctionRepository: AuctionRepository
) : QueryHandler<GetAuctionsQuery, GetAuctionsQuery.AuctionsDTO> {

    override suspend fun handle(query: GetAuctionsQuery): Deferred<ResultT<GetAuctionsQuery.AuctionsDTO>> =
        suspendedResultActionOfT {
//            GetAuctionsQuery.AuctionsDTO(
//                auctionRepository.getAll().map {
//                    it.toDTO(getUtcTimeNow())
//                }
//            )
            null
        }
}

@Serializable
class GetAuctionsQuery {
    @Serializable
    data class AuctionsDTO(val auctions: List<GetAuctionQuery.AuctionDTO>)
}
