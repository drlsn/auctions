package com.slaycard.basic.cqrs

import com.slaycard.basic.Result

interface CommandHandler<TCommand> {
    fun handle(command: TCommand): Result
}
