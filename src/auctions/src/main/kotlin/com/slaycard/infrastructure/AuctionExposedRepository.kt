package com.slaycard.infrastructure

import Auction
import AuctionId
import com.slaycard.basic.getUtcTimeNow
import com.slaycard.entities.roots.AuctionRepository
import com.slaycard.entities.shared.AuctionItemId
import com.slaycard.entities.shared.Money
import com.slaycard.entities.shared.UserId
import com.slaycard.infrastructure.data.AuctionsTable
import com.slaycard.useCases.GetAuctionQuery
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import java.util.*

class AuctionExposedRepository : AuctionRepository {

    override suspend fun getById(id: AuctionId): Deferred<Auction?> =
        dbQuery {
            val uuid =
                try { UUID.fromString(id.value) }
                catch (ex: IllegalArgumentException) { return@dbQuery null }

            val row = AuctionsTable
                .select(AuctionsTable.id eq uuid)
                .firstOrNull()

            row?.toAuction()
        }

    override suspend fun add(auction: Auction): Deferred<Boolean> =
        dbQuery {
            val result = AuctionsTable.insert {
                row ->

                row[id] = UUID.fromString(auction.id.value)
                row[version] = 0
                row[sellingUserId] = UUID.fromString(auction.sellingUserId.value)
                row[auctionItemName] = auction.auctionItemName
                row[auctionItemId] = UUID.fromString(auction.auctionItemId.value)
                row[quantity] = auction.quantity
                row[startingPrice] = auction.startingPrice.value
                row[currentPrice] = auction.currentPrice.value
                row[startTime] = auction.startTime.toJavaLocalDateTime()
                row[originalDurationHours] = auction.originalDurationHours

                if (auction.lastBiddingUserId != null)
                    row[lastBiddingUserId] = UUID.fromString(auction.lastBiddingUserId!!.value)

                if (auction.cancelTime != null)
                    row[cancelTime] = auction.cancelTime!!.toJavaLocalDateTime()

                row[description] = auction.description
            }

            result.insertedCount == 1
        }

    override suspend fun update(entity: Auction): Deferred<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: AuctionId): Deferred<Boolean> {
        TODO("Not yet implemented")
    }
}

suspend fun<TResult> dbQuery(f: () -> TResult): Deferred<TResult> =
    suspendedTransactionAsync(Dispatchers.IO) { f() }

fun ResultRow.toAuction(): Auction {
    return Auction(
        AuctionId(this[AuctionsTable.id].toString()),
        UserId(this[AuctionsTable.sellingUserId].toString()),
        this[AuctionsTable.auctionItemName],
        AuctionItemId(this[AuctionsTable.auctionItemId].toString()),
        this[AuctionsTable.quantity],
        Money(this[AuctionsTable.startingPrice]),
        this[AuctionsTable.originalDurationHours],
        this[AuctionsTable.description],
        Money(this[AuctionsTable.currentPrice]),
        this[AuctionsTable.startTime].toKotlinLocalDateTime(),
        timeNow = getUtcTimeNow()
    )
}
