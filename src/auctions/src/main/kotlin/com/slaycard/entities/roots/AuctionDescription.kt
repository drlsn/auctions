package com.slaycard.entities.roots

import AuctionId
import PropertyList
import com.slaycard.basic.domain.Entity

class AuctionDescription(
    id: AuctionId,
    val description: String = "",                                   // Modifiable
    val properties: PropertyList = emptyList(),
) : Entity<AuctionId>(id) {

}