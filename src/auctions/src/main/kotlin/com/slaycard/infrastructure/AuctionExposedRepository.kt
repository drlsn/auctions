package com.slaycard.infrastructure

import Auction
import AuctionId
import com.slaycard.entities.roots.AuctionRepository
import com.slaycard.infrastructure.data.AuctionsTable
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.toJavaLocalDateTime
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import java.util.*

class AuctionExposedRepository : AuctionRepository {

    //    suspend fun getById(id: AuctionId): Deferred<Auction?> = dbQuery {
//        val uuid =
//            try { UUID.fromString(id.value) }
//            catch (ex: IllegalArgumentException) { return@dbQuery null }
//
//        val row = AuctionBidTable
//            .select(AuctionBidTable.id eq uuid)
//            .firstOrNull() ?:
//            return@dbQuery null
//
//        Auction(id)
//    }
//
//    suspend fun add(auction: Auction): Deferred<Boolean> = dbQuery {
//        val uuid =
//            try { UUID.fromString(auction.id.value) }
//            catch (ex: IllegalArgumentException) { return@dbQuery false }
//
//        AuctionBidTable.insert {
//            row -> row[id] = uuid
//        }
//
//        true
//    }
    override suspend fun getById(id: AuctionId): Deferred<Auction> {
        TODO("Not yet implemented")
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
                row[properties] = auction.properties
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
