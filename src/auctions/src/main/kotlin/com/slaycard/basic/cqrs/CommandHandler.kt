package com.slaycard.basic.cqrs

import com.slaycard.basic.Result
import kotlinx.coroutines.Deferred

interface CommandHandler<TCommand> {
    suspend fun handle(command: TCommand): Deferred<Result>
}
