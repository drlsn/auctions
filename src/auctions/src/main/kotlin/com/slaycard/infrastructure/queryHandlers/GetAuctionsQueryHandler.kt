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
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class GetAuctionsQueryHandler : QueryHandler<GetAuctionsQuery, GetAuctionsQuery.AuctionsDTO> {

    override suspend fun handle(query: GetAuctionsQuery): Deferred<ResultT<GetAuctionsQuery.AuctionsDTO>> =
        suspendedResultActionOfT {
            dbQuery {
                GetAuctionsQuery.AuctionsDTO(
                    AuctionsTable.selectAll().map { row ->
                        val startTime = row[AuctionsTable.startTime].toKotlinLocalDateTime()
                        val durationHours = row[AuctionsTable.originalDurationHours]

                        val cancelTime = row[AuctionsTable.cancelTime]?.toKotlinLocalDateTime()

                        val timeNow = getUtcTimeNow()
                        val isFinished = Auction.isFinished(timeNow, Auction.getEndTime(startTime, durationHours, cancelTime))

                        row.toDTO(timeNow, isFinished, cancelTime)
                    }
                )
            }.await()
        }
}

fun ResultRow.toDTO(timeNow: LocalDateTime, isFinished: Boolean, cancelTime: LocalDateTime?): GetAuctionQuery.AuctionDTO =
    GetAuctionQuery.AuctionDTO(
        this[AuctionsTable.id].toString(),
        this[AuctionsTable.sellingUserId].toString(),
        this[AuctionsTable.auctionItemName],
        this[AuctionsTable.description],
        this[AuctionsTable.quantity],
        this[AuctionsTable.startingPrice],
        this[AuctionsTable.currentPrice],
        this[AuctionsTable.startTime].toString(),
        this[AuctionsTable.originalDurationHours],
        isFinished,
        isCancelled = cancelTime != null,
        events = emptyList(),
        this[AuctionsTable.lastBiddingUserId].toString(),
        cancelTime = cancelTime?.toString())
