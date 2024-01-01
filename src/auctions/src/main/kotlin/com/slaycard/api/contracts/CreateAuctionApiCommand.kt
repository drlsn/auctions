package com.slaycard.api.contracts

import PropertyList
import kotlinx.serialization.Serializable

@Serializable
data class CreateAuctionApiCommand(
    val name: String,
    val startingPrice: Int,
    val itemId: String,
    val sellingUserId: String,
    val quantity: Int,
    val durationHours: Int = 72,
    val description: String? = null)
