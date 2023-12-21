package com.slaycard.basic.domain

import com.slaycard.basic.getUtcTimeNow
import com.slaycard.basic.uuid64
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
abstract class DomainEvent {
    abstract val id: String
    abstract val utcTimeOccurred: LocalDateTime
}
