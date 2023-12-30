package com.slaycard.infrastructure

import Auction
import AuctionId
import com.slaycard.entities.roots.AuctionRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync

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

    override suspend fun add(entity: Auction): Deferred<Boolean> {
        TODO("Not yet implemented")
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
