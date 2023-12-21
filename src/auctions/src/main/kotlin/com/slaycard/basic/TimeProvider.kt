package com.slaycard.basic

import kotlinx.datetime.*
import java.lang.Exception
import java.time.Duration
import java.time.Period
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun getUtcTimeNow(): LocalDateTime = LocalDateTime.parse(
    ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))

operator fun LocalDateTime.plus(other: DateTimePeriod): LocalDateTime {
    val zonedDateTime = ZonedDateTime.of(
        this.year, this.monthNumber, this.dayOfMonth, this.hour, this.minute, this.second, this.nanosecond,
        ZoneId.of("UTC"))

    val newZonedTime = zonedDateTime +
        Duration.ofNanos(other.nanoseconds.toLong()) +
        Duration.ofSeconds(other.seconds.toLong()) +
        Duration.ofMinutes(other.minutes.toLong()) +
        Duration.ofHours(other.hours.toLong()) +
        Duration.ofDays(other.days.toLong()) +
        Period.ofMonths(other.months) +
        Period.ofYears(other.years)

    return LocalDateTime.parse(newZonedTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
}
