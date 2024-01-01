package com.slaycard.useCases

import AuctionId
import com.slaycard.entities.shared.Money
import com.slaycard.basic.cqrs.CommandHandler
import com.slaycard.entities.roots.AuctionRepository
import kotlinx.serialization.Serializable
import com.slaycard.basic.Result
import com.slaycard.basic.addTo
import com.slaycard.basic.suspendedResultActionOfT
import com.slaycard.entities.shared.UserId
import kotlinx.coroutines.Deferred

class OutbidAuctionCommandHandler(
    private val auctionRepository: AuctionRepository
) : CommandHandler<OutbidAuctionCommand> {

    override suspend fun handle(command: OutbidAuctionCommand): Deferred<Result> =
        suspendedResultActionOfT { result ->
            val auction = auctionRepository.getById(AuctionId(command.auctionId)).await()
            if (auction == null) {
                result.fail("Could not find the auction");
                return@suspendedResultActionOfT
            }

            auction.outbid(Money(command.money), UserId(command.userId)).addTo(result)
            if (!result.isSuccess)
               return@suspendedResultActionOfT

            if (!auctionRepository.update(auction).await())
                result.fail("Could not store the changes")
        }
}

@Serializable
data class OutbidAuctionCommand(
    val userId: String,
    val auctionId: String,
    val money: Int)
