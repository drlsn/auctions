package com.slaycard.useCases

import Auction
import AuctionId
import com.slaycard.basic.*
import com.slaycard.basic.cqrs.QueryHandler
import com.slaycard.basic.domain.DomainEvent
import com.slaycard.entities.roots.AuctionRepository
import kotlinx.coroutines.Deferred
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

class GetAuctionQueryHandler(
    private val auctionRepository: AuctionRepository
) : QueryHandler<GetAuctionQuery, GetAuctionQuery.AuctionDTO> {

    override suspend fun handle(query: GetAuctionQuery): Deferred<ResultT<GetAuctionQuery.AuctionDTO>> =
        suspendedResultActionOfT {
            if (query.auctionId.isEmpty())
                it.fail("The id must be of proper format")

            val auction = auctionRepository.getById(AuctionId(query.auctionId)).await()
            if (auction == null) {
                it.fail("The auction does not exist");
                return@suspendedResultActionOfT null
            }

            auction.toDTO(getUtcTimeNow())
        }
}

@Serializable
data class GetAuctionQuery(val auctionId: String) {
    @Serializable
    data class AuctionDTO(
        val id: String,
        val sellingUserId: String,
        val itemName: String,
        val description: String,
        val quantity: Int,
        val startingPrice: Int,
        val currentPrice: Int,
        val startTime: String,
        val originalDurationHours: Int,
        val isFinished: Boolean,
        val isCancelled: Boolean,
        val events: List<DomainEvent>,
        val winnerId: String?,
        val cancelTime: String?)
}

fun Auction.toDTO(timeNow: LocalDateTime): GetAuctionQuery.AuctionDTO =
    GetAuctionQuery.AuctionDTO(
        this.id.value,
        this.sellingUserId.value,
        this.auctionItemName,
        this.description,
        this.quantity,
        this.startingPrice.value,
        this.currentPrice.value,
        this.startTime.toString(),
        this.originalDurationHours,
        this.isFinished(timeNow),
        this.isCancelled(),
        this.events,
        this.lastBiddingUserId?.value,
        cancelTime = if (this.isCancelled()) this.endTime.toString() else null)