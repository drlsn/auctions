package com.slaycard.basic.domain

interface Repository<TEntity, TId>
    where TEntity: Entity<TId> {

    fun get(id: TId): TEntity?
    fun getAll(): List<TEntity>
    fun add(entity: TEntity): Boolean
    fun update(entity: TEntity): Boolean
    fun delete(id: TId): Boolean
}
