package com.slaycard.infrastructure.data

import PropertyList
import com.slaycard.infrastructure.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.UUIDTable

object AuctionDescriptionsTable : UUIDTable("auctionDescriptions") {
    val description = varchar("description", 2000)
    val properties = json<PropertyList>(
        "propertyList",
        { Json.encodeToString(it as PropertyList) },
        { Json.decodeFromString(it) as PropertyList })
}