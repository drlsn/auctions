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
import com.slaycard.entities.shared.UserId

class OutbidAuctionCommandHandler(
    private val auctionRepository: AuctionRepository<Auction, AuctionId>
)
    : CommandHandler<OutbidAuctionCommand> {

    override fun handle(command: OutbidAuctionCommand): Result =
        resultAction { result ->
            val auction = auctionRepository.getById(AuctionId(command.auctionId))
            if (auction == null) {
                result.fail("Could not find the auction");
                return@resultAction
            }

            auction.outbid(Money(command.money), UserId(command.userId)).addTo(result)
            if (!result.isSuccess)
               return@resultAction

            if (!auctionRepository.update(auction))
                result.fail("Could not store the changes")
        }
}

@Serializable
data class OutbidAuctionCommand(
    val userId: String,
    val auctionId: String,
    val money: Int)
