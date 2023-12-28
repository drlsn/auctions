package com.slaycard.entities.roots

import AuctionId
import kotlinx.coroutines.Deferred

interface AuctionDescriptionRepository {

    suspend fun getById(id: AuctionId): Deferred<AuctionDescription>
    suspend fun add(entity: AuctionDescription): Deferred<Boolean>
    suspend fun update(entity: AuctionDescription): Deferred<Boolean>
    suspend fun delete(id: AuctionId): Deferred<Boolean>

}
