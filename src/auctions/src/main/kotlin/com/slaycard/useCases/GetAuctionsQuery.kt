package com.slaycard.useCases

import kotlinx.serialization.Serializable

@Serializable
class GetAuctionsQuery {
    @Serializable
    data class AuctionsDTO(val auctions: List<GetAuctionQuery.AuctionDTO>)
}
