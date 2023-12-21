package com.slaycard.basic

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun getUtcTimeNow(): LocalDateTime = LocalDateTime.parse(
    ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))

operator fun LocalDateTime.plus(other: DateTimePeriod): LocalDateTime =
    LocalDateTime(
        this.year + other.years,
        this.monthNumber + other.months,
        this.dayOfMonth + other.days,
        this.hour + other.hours,
        this.minute + other.minutes,
        this.second + other.seconds,
        this.nanosecond + other.nanoseconds)