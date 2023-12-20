package com.slaycard.api.plugins

import com.slaycard.basic.cqrs.CommandHandler
import com.slaycard.basic.cqrs.QueryHandler
import com.slaycard.infrastructure.RequestScope
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent

fun executeScopedAction(f: (Scope) -> Unit) =
    KoinJavaComponent.getKoin()
        .createScope<RequestScope>()
        .also { f(it); it.close() }

inline fun<reified TApiCommand: Any, TAppCommand, reified TAppCommandHandler: CommandHandler<TAppCommand>>
        PipelineContext<Unit, ApplicationCall>.executeCommandHandler(crossinline commandTransformer: (TApiCommand) -> TAppCommand) =
    executeScopedAction {
        launch {
            val command = call.receive<TApiCommand>()
            val handler = it.get<TAppCommandHandler>()
            val result = handler.handle(commandTransformer(command))
            when (result.isSuccess) {
                true -> call.response.status(HttpStatusCode.Created)
                false -> call.response.status(HttpStatusCode.BadRequest)
            }

            call.respond(result)
        }
    }

inline fun<reified TApiQuery: Any, TAppQuery, reified TAppQueryOut, reified TAppQueryHandler: QueryHandler<TAppQuery, TAppQueryOut>>
        PipelineContext<Unit, ApplicationCall>.executeQueryHandler(crossinline queryTransformer: (Parameters) -> TAppQuery) =
    executeScopedAction {
        launch {
            //val parameters = call.receiveParameters()
            val handler = it.get<TAppQueryHandler>()
            val result = handler.handle(queryTransformer(call.parameters))
            when (result.isSuccess) {
                true -> call.response.status(HttpStatusCode.OK)
                false -> call.response.status(HttpStatusCode.BadRequest)
            }

            call.respond(result)
        }
    }
