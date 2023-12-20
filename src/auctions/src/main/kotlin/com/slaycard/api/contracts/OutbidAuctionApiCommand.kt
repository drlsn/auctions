package com.slaycard.api.contracts

import kotlinx.serialization.Serializable

@Serializable
data class OutbidAuctionApiCommand(val newPrice: Int)