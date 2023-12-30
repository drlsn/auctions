package com.slaycard.useCases

import com.slaycard.basic.Result
import com.slaycard.basic.cqrs.CommandHandler
import com.slaycard.basic.suspendedResultAction
import kotlinx.coroutines.Deferred
import kotlinx.serialization.Serializable

class CreateAuctionCommandHandler : CommandHandler<CreateAuctionCommand> {

    override suspend fun handle(command: CreateAuctionCommand): Deferred<Result> =
        suspendedResultAction { result ->



        }
}

@Serializable
data class CreateAuctionCommand(
    val name: String,
    val originalPrice: Int,
    val description: String? = null)