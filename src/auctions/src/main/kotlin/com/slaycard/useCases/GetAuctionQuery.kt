package com.slaycard.useCases

import Auction
import AuctionId
import com.slaycard.basic.*
import com.slaycard.basic.cqrs.QueryHandler
import com.slaycard.basic.domain.DomainEvent
import com.slaycard.entities.roots.AuctionRepository
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

class GetAuctionQueryHandler(
    private val auctionRepository: AuctionRepository<Auction, AuctionId>
)
    : QueryHandler<GetAuctionQuery, GetAuctionQuery.AuctionDTO> {

    override fun handle(query: GetAuctionQuery): ResultT<GetAuctionQuery.AuctionDTO> =
        resultActionOfT {
            if (query.auctionId.isEmpty())
                it.fail("The id must be of proper format")

            val auction = auctionRepository.getById(AuctionId(query.auctionId))
            if (auction == null) {
                it.fail("The auction does not exist");
                return@resultActionOfT null
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
        val hasFinished: Boolean,
        val wasCancelled: Boolean,
        val events: List<DomainEvent>,
        val winnerId: String?,
        val cancelTime: String?)
}

fun Auction.toDTO(timeNow: LocalDateTime): GetAuctionQuery.AuctionDTO =
    GetAuctionQuery.AuctionDTO(
        this.id.value,
        this.sellingUser.value,
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
        this.lastBiddingUser?.value,
        cancelTime = if (this.isCancelled()) this.endTime.toString() else null)