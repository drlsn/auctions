package com.slaycard.basic

import kotlinx.datetime.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun getUtcTimeNow(): LocalDateTime = LocalDateTime.parse(
    ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))