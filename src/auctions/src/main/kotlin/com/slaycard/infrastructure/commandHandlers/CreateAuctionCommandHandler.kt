package com.slaycard.infrastructure.commandHandlers

import Auction
import com.slaycard.basic.Result
import com.slaycard.basic.addTo
import com.slaycard.basic.cqrs.CommandHandler
import com.slaycard.basic.suspendedResultAction
import com.slaycard.entities.shared.Money
import com.slaycard.infrastructure.AuctionBidTable
import com.slaycard.infrastructure.AuctionDescriptionsTable
import com.slaycard.infrastructure.dbQuery
import com.slaycard.useCases.CreateAuctionCommand
import kotlinx.coroutines.Deferred
import org.jetbrains.exposed.sql.insert
import java.util.*

class CreateAuctionCommandHandler : CommandHandler<CreateAuctionCommand> {

    override suspend fun handle(command: CreateAuctionCommand): Deferred<Result> =
        suspendedResultAction { result ->

            val auction = Auction(
                auctionItemName = command.name,
                startingPrice = Money(command.originalPrice))

            if (!auction.validate().addTo(result).isSuccess)
                return@suspendedResultAction

            val writeResult = dbQuery {
                val uuid =
                    try { UUID.fromString(auction.id.value) }
                    catch (ex: IllegalArgumentException) { return@dbQuery false }

                AuctionBidTable.insert {
                    row -> row[id] = uuid
                }

                AuctionDescriptionsTable.insert {row ->
                    row[id] = uuid
                    row[description] = command.description ?: ""
                    row[properties] = emptyList()
                }

                true
            }.await()

            if (!writeResult)
                result.fail("Could not create the auction")
            // val auctionBidsTracker = AuctionBidsTracker(..)
            // val auctionInfo = AuctionInfo(..)

//            if (!auctionRepository.add(auction))
//                result.fail("Could not create the auction")auction
        }
}
