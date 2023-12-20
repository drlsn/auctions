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
        resultAction { result ->
            if (command.name.isEmpty())
                result.fail("The name must contain characters")

            if (command.originalPrice <= 0)
                result.fail("The starting price must be greater than zero")

            if (!result.isSuccess)
                return@resultAction

            if (!auctionRepository.add(
                Auction(
                    AuctionId(UUID.randomUUID().toString()),
                    AuctionItemId(UUID.randomUUID().toString()),
                    quantity = 1,
                    Money(command.originalPrice),
                    command.name))) {
                result.fail("Could not create the auction")
            }
        }
}

@Serializable
data class CreateAuctionCommand(val name: String, val originalPrice: Int)