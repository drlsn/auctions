package com.slaycard.infrastructure.queryHandlers

import Auction
import com.slaycard.basic.ResultT
import com.slaycard.basic.cqrs.QueryHandler
import com.slaycard.basic.getUtcTimeNow
import com.slaycard.basic.suspendedResultActionOfT
import com.slaycard.infrastructure.data.AuctionsTable
import com.slaycard.infrastructure.dbQuery
import com.slaycard.useCases.GetAuctionQuery
import com.slaycard.useCases.GetAuctionsQuery
import kotlinx.coroutines.Deferred
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class GetAuctionsQueryHandler : QueryHandler<GetAuctionsQuery, GetAuctionsQuery.AuctionsDTO> {

    override suspend fun handle(query: GetAuctionsQuery): Deferred<ResultT<GetAuctionsQuery.AuctionsDTO>> =
        suspendedResultActionOfT {
            dbQuery {
                GetAuctionsQuery.AuctionsDTO(
                    AuctionsTable.selectAll().map { row ->
                        row.toDTO()
                    }
                )
            }.await()
        }
}

fun ResultRow.toDTO(): GetAuctionQuery.AuctionDTO {
    val startTime = this[AuctionsTable.startTime].toKotlinLocalDateTime()
    val durationHours = this[AuctionsTable.originalDurationHours]

    val cancelTime = this[AuctionsTable.cancelTime]?.toKotlinLocalDateTime()

    val timeNow = getUtcTimeNow()
    val isFinished = Auction.isFinished(timeNow, Auction.getEndTime(startTime, durationHours, cancelTime))

    return GetAuctionQuery.AuctionDTO(
        this[AuctionsTable.id].toString(),
        this[AuctionsTable.sellingUserId].toString(),
        this[AuctionsTable.auctionItemName],
        this[AuctionsTable.description],
        this[AuctionsTable.quantity],
        this[AuctionsTable.startingPrice],
        this[AuctionsTable.currentPrice],
        startTime.toString(),
        durationHours,
        isFinished,
        isCancelled = cancelTime != null,
        this[AuctionsTable.lastBiddingUserId].toString(),
        cancelTime = cancelTime?.toString()
    )
}