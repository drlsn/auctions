package com.slaycard.infrastructure

import Auction
import AuctionId
import com.slaycard.infrastructure.data.AuctionBidTable
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.Function
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.vendors.PostgreSQLDialect
import org.jetbrains.exposed.sql.vendors.currentDialect
import org.postgresql.util.PGobject
import java.util.*
import kotlin.reflect.KClass

class AuctionExposedRepository {

    suspend fun getById(id: AuctionId): Deferred<Auction?> = dbQuery {
        val uuid =
            try { UUID.fromString(id.value) }
            catch (ex: IllegalArgumentException) { return@dbQuery null }

        val row = AuctionBidTable
            .select(AuctionBidTable.id eq uuid)
            .firstOrNull() ?:
            return@dbQuery null

        Auction(id)
    }

    suspend fun add(auction: Auction): Deferred<Boolean> = dbQuery {
        val uuid =
            try { UUID.fromString(auction.id.value) }
            catch (ex: IllegalArgumentException) { return@dbQuery false }

        AuctionBidTable.insert {
            row -> row[id] = uuid
        }

        true
    }

}

suspend fun<TResult> dbQuery(f: () -> TResult): Deferred<TResult> =
    suspendedTransactionAsync(Dispatchers.IO) { f() }
