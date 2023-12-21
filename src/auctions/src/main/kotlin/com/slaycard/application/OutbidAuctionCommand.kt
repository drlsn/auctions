package com.slaycard.application

import Auction
import AuctionId
import com.slaycard.entities.Money
import com.slaycard.basic.cqrs.CommandHandler
import com.slaycard.basic.Repository
import kotlinx.serialization.Serializable
import com.slaycard.basic.Result
import com.slaycard.basic.addTo
import com.slaycard.basic.resultAction

class OutbidAuctionCommandHandler(
    private val auctionRepository: Repository<Auction, AuctionId>)
    : CommandHandler<OutbidAuctionCommand> {

    override fun handle(command: OutbidAuctionCommand): Result =
        resultAction { result ->
            val auction = auctionRepository.get(AuctionId(command.auctionId))
            if (auction == null) {
                result.fail("Could not find the auction");
                return@resultAction
            }

            auction.outbid(Money(command.money)).addTo(result)
            if (!result.isSuccess)
               return@resultAction

            if (!auctionRepository.update(auction))
                result.fail("Could not store the changes")
        }
}

@Serializable
data class OutbidAuctionCommand(
    val auctionId: String,
    val money: Int)
