package com.slaycard.useCases

import Auction
import AuctionId
import PropertyList
import com.slaycard.basic.Result
import com.slaycard.basic.addTo
import com.slaycard.basic.cqrs.CommandHandler
import com.slaycard.basic.suspendedResultAction
import com.slaycard.basic.uuid64
import com.slaycard.entities.roots.AuctionRepository
import com.slaycard.entities.shared.AuctionItemId
import com.slaycard.entities.shared.Money
import com.slaycard.entities.shared.UserId
import kotlinx.coroutines.Deferred
import kotlinx.serialization.Serializable

class CreateAuctionCommandHandler(
    private val auctionRepository: AuctionRepository
) : CommandHandler<CreateAuctionCommand> {

    override suspend fun handle(command: CreateAuctionCommand): Deferred<Result> =
        suspendedResultAction { result ->

            val auction = Auction(
                AuctionId(uuid64()),
                UserId(command.sellingUserId),
                command.name,
                AuctionItemId(command.itemId),
                command.quantity,
                Money(command.startingPrice),
                command.originalDurationHours,
                command.description ?: "",
                command.properties ?: emptyList())

            result.add(auction.validate())
            if (!result.isSuccess)
                return@suspendedResultAction

            if (!auctionRepository.add(auction).await())
                result.fail("Could not add auction to database")
        }
}

@Serializable
data class CreateAuctionCommand(
    val name: String,
    val startingPrice: Int,
    val itemId: String,
    val sellingUserId: String,
    val quantity: Int,
    val originalDurationHours: Int = 72,
    val description: String? = null,
    val properties: PropertyList? = null)