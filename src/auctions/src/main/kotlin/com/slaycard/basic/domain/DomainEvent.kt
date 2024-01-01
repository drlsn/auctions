package com.slaycard.basic.domain

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
abstract class DomainEvent {
    abstract val id: String
    abstract val utcTimeOccurred: LocalDateTime
}
