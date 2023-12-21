package com.slaycard.basic.domain

abstract class Entity<TId>(
    val id: TId,
    internal var version: Int = 0) {

    val events: MutableList<DomainEvent> = mutableListOf()

}