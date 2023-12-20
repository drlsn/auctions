package com.slaycard.api.contracts

import kotlinx.serialization.Serializable

@Serializable
data class GetAuctionApiQuery(val id: String)