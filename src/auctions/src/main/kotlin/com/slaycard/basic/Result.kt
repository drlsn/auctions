package com.slaycard.basic

import kotlinx.serialization.Serializable

@Serializable
open class Result(
    open val messages: MutableList<Message> = mutableListOf()) {

    open val isSuccess: Boolean
        get() = messages
            .flatMap{ it.subMessages }
            .all{ it.level != MessageLevel.error }

    operator fun plus(other: Result): Result =
        Result((this.messages + other.messages).toMutableList())

    fun fail(message: String) =
        messages.add(Message(MessageLevel.error, message))
}

@Serializable
class ResultT<T>(
    val value: T?) : Result() {

    override val isSuccess: Boolean
        get() = value != null && super.isSuccess

    fun with(other: Result): ResultT<T> {
        messages.addAll(other.messages)
        return this
    }
}

fun resultAction(f: (result: Result) -> Unit): Result {
    var result = Result()
    f(result)
    return result
}

inline fun<reified T> resultActionOfT(f: (result: Result) -> T?): ResultT<T> {
    var result = Result()
    return ResultT(f(result)).with(result)
}

class ResultOf<T>(messages: MutableList<Message>) : Result(messages) {}

fun failure(message: String) = Result(mutableListOf(Message(MessageLevel.error, message)))
fun success() = Result()

@Serializable
data class Message(val level: MessageLevel, val content: String, val subMessages: List<Message> = emptyList())

@Serializable
data class MessageLevel(val name: String) {

    companion object {
        val info: MessageLevel = MessageLevel("info")
        val warning: MessageLevel = MessageLevel("warning")
        val error: MessageLevel = MessageLevel("error")
    }

}

