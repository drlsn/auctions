package com.slaycard.useCases

import Auction
import AuctionId
import com.slaycard.entities.shared.Money
import com.slaycard.basic.cqrs.CommandHandler
import com.slaycard.entities.roots.AuctionRepository
import kotlinx.serialization.Serializable
import com.slaycard.basic.Result
import com.slaycard.basic.addTo
import com.slaycard.basic.resultAction

class CreateAuctionCommandHandler(
    private val auctionRepository: AuctionRepository<Auction, AuctionId>
)
    : CommandHandler<CreateAuctionCommand> {

    override fun handle(command: CreateAuctionCommand): Result =
        resultAction { result ->

            // val auctionBidsTracker = AuctionBidsTracker(..)
            // val auctionInfo = AuctionInfo(..)

            val auction = Auction(auctionItemName = command.name, startingPrice = Money(command.originalPrice))
            if (!auction.validate().addTo(result).isSuccess)
                return@resultAction

            if (!auctionRepository.add(auction))
                result.fail("Could not create the auction")
        }
}

@Serializable
data class CreateAuctionCommand(val name: String, val originalPrice: Int)