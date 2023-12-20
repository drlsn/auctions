package com.slaycard.api.plugins

import com.slaycard.basic.Result
import com.slaycard.basic.cqrs.CommandHandler
import com.slaycard.basic.cqrs.QueryHandler
import com.slaycard.infrastructure.RequestScope
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.launch
import nl.adaptivity.xmlutil.core.impl.multiplatform.name
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent
import java.lang.reflect.Constructor

fun executeScopedAction(f: (Scope) -> Unit) =
    KoinJavaComponent.getKoin()
        .createScope<RequestScope>()
        .also { f(it); it.close() }

inline fun<reified TApiCommand: Any, TAppCommand, reified TAppCommandHandler: CommandHandler<TAppCommand>>
    PipelineContext<Unit, ApplicationCall>.executeCommandHandler(
        crossinline commandTransformer: (routeParameters: Parameters, apiCommand: TApiCommand) -> TAppCommand) =
    executeScopedAction {
        launch {
            val command = call.receive<TApiCommand>()
            val handler = it.get<TAppCommandHandler>()
            val result = handler.handle(commandTransformer(call.parameters, command))
            when (result.isSuccess) {
                true -> call.response.status(HttpStatusCode.Created)
                false -> call.response.status(HttpStatusCode.BadRequest)
            }

            call.respond(result)
        }
    }

inline fun<reified TApiQuery: Any, TAppQuery, reified TAppQueryOut, reified TAppQueryHandler: QueryHandler<TAppQuery, TAppQueryOut>>
    PipelineContext<Unit, ApplicationCall>.executeQueryHandler(
        crossinline queryTransformer: (routeParameters: Parameters, queryParameters: Parameters, result: Result) -> TAppQuery?) =
    executeScopedAction {
        launch {
            val handler = it.get<TAppQueryHandler>()

            val apiResult = Result()
            val query = queryTransformer(call.parameters, call.request.queryParameters, apiResult)
            if (query == null || !apiResult.isSuccess) {
                call.response.status(HttpStatusCode.BadRequest)
                call.respond(apiResult)
                return@launch
            }

            val result = handler.handle(query)
            when (result.isSuccess) {
                true -> call.response.status(HttpStatusCode.OK)
                false -> call.response.status(HttpStatusCode.BadRequest)
            }

            call.respond(result)
        }
    }

//inline fun<reified T> ApplicationCall.receiveParameters(): T? {
//    println("DOPE 1:")
//    val cs = T::class.java.constructors
//    val args = T::class.java.constructors.first().parameters.map {
//        println("Inside: ")
//        if (it.name == null || it.type.kotlin.isValue || !this.parameters.contains(it.name!!))
//            return null
//
//        if (!this.parameters.contains(it.name!!))
//            return@map null
//
//        this.parameters[it.name!!]
//    }
//    println("DOPE 2: $args")
//
//    val clazz = Class.forName(T::class.name)
//    val constructor: Constructor<*> = clazz.getConstructor(*args.map { T::class.java }.toTypedArray())
//    val instance = constructor.newInstance(args)
//
//    return instance as T
//}
//
