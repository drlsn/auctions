package com.slaycard.useCases

import kotlinx.serialization.Serializable

@Serializable
data class CreateAuctionCommand(
    val name: String,
    val originalPrice: Int,
    val description: String? = null)