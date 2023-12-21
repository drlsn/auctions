package com.slaycard.basic.domain

import com.slaycard.basic.getUtcTimeNow
import com.slaycard.basic.uuid64
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
open class DomainEvent(
    val id: String = uuid64(),
    val utcTimeOccurred: LocalDateTime = getUtcTimeNow()
)