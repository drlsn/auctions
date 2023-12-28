package com.slaycard.basic

import com.slaycard.basic.Message.Companion.containErrors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
open class Result(
    open val messages: MutableList<Message> = mutableListOf()) {

    open val isSuccess: Boolean
        get() = !messages.containErrors()

    operator fun plus(other: Result): Result =
        Result((this.messages + other.messages).toMutableList())

    fun fail(message: String): Boolean =
        messages.add(Message(MessageLevel.error, message))

    fun add(other: Result): Boolean =
        messages.addAll(other.messages)
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

suspend fun suspendedResultAction(f: suspend (result: Result) -> Unit): Deferred<Result> {
    val result = Result()
    val a = async {
        f(result)
    }.await()
    return result
}

class ResultOf<T>(messages: MutableList<Message>) : Result(messages) {}

fun failure(message: String) = Result(mutableListOf(Message(MessageLevel.error, message)))
fun success() = Result()

fun Result.addTo(other: Result): Result {
    other.add(this)
    return this
}

@Serializable
data class Message(val level: MessageLevel, val content: String, val subMessages: List<Message> = emptyList()) {

    companion object {
        fun Iterable<Message>.containErrors(): Boolean =
            this.any{
                it.level == MessageLevel.error || it.subMessages.containErrors()
            }
    }
}

@Serializable
data class MessageLevel(val name: String) {

    companion object {
        val info: MessageLevel = MessageLevel("info")
        val warning: MessageLevel = MessageLevel("warning")
        val error: MessageLevel = MessageLevel("error")
    }

}

