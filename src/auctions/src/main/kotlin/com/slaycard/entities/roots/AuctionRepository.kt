package com.slaycard.entities.roots

import Auction
import AuctionId
import kotlinx.coroutines.Deferred

interface AuctionRepository {

    suspend fun getById(id: AuctionId): Deferred<Auction?>
    suspend fun add(entity: Auction): Deferred<Boolean>
    suspend fun update(entity: Auction): Deferred<Boolean>
    suspend fun delete(id: AuctionId): Deferred<Boolean>

}
