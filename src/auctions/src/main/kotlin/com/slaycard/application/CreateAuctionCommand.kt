package com.slaycard.application

import Auction
import AuctionId
import AuctionItemId
import Money
import com.slaycard.basic.cqrs.CommandHandler
import com.slaycard.basic.Repository
import kotlinx.serialization.Serializable
import com.slaycard.basic.Result
import com.slaycard.basic.resultAction
import java.util.*

class CreateAuctionCommandHandler(
    private val auctionRepository: Repository<Auction, AuctionId>)
    : CommandHandler<CreateAuctionCommand> {

    override fun handle(command: CreateAuctionCommand): Result =
        resultAction {
            val result = Auction.create(command.name, Money(command.originalPrice))
            if (result.isSuccess || result.value == null)
                return@resultAction

            if (!auctionRepository.add(result.value))
                result.fail("Could not create the auction")
        }
}

@Serializable
data class CreateAuctionCommand(val name: String, val originalPrice: Int)