package com.slaycard.entities.roots

import com.slaycard.basic.domain.Entity

interface AuctionRepository<TEntity, TId>
    where TEntity: Entity<TId> {

    suspend fun getById(id: TId): TEntity?
    suspend fun add(entity: TEntity): Boolean
    suspend fun update(entity: TEntity): Boolean
    suspend fun delete(id: TId): Boolean
}
