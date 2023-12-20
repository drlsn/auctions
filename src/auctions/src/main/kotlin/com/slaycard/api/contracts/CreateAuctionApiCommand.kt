package com.slaycard.api.contracts

import kotlinx.serialization.Serializable

@Serializable
data class CreateAuctionApiCommand(val name: String, val originalPrice: Int)