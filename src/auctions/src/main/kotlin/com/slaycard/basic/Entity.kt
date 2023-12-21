package com.slaycard.basic

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

abstract class Entity<TId>(
    val id: TId,
    internal var version: Int = 0) {

    val events: MutableList<DomainEvent> = mutableListOf()

}

@Serializable
open class DomainEvent(
    val id: String = uuid64(),
    val utcTimeOccurred: LocalDateTime = getUtcTimeNow()
)
