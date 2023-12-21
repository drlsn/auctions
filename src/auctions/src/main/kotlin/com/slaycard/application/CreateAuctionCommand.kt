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

class CreateAuctionCommandHandler(
    private val auctionRepository: Repository<Auction, AuctionId>)
    : CommandHandler<CreateAuctionCommand> {

    override fun handle(command: CreateAuctionCommand): Result =
        resultAction { result ->
            val auction = Auction.createDefault(command.name, Money(command.originalPrice))
            if (!auction.validate().addTo(result).isSuccess)
                return@resultAction

            if (!auctionRepository.add(auction))
                result.fail("Could not create the auction")
        }
}

@Serializable
data class CreateAuctionCommand(val name: String, val originalPrice: Int)