package com.slaycard.api.plugins

import com.slaycard.application.AuctionsService
import com.slaycard.infrastructure.RequestScope
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent.getKoin

const val AUCTIONS_ROUTE = "/auctions"

fun Application.configureRouting() {
    routing {
        get(AUCTIONS_ROUTE) {
            executeScopedAction {
                launch {
                    call.respond(
                        it.get<AuctionsService>().getAll())
                }
            }
        }

        post(AUCTIONS_ROUTE) {
            executeScopedAction {
                launch {
                    val command = call.receive<AuctionsService.CreateAuctionCommandIn>()
                    when (it.get<AuctionsService>().add(command)) {
                        true -> call.response.status(HttpStatusCode.OK)
                        false -> call.response.status(HttpStatusCode.BadRequest)
                    }
                }
            }
        }
    }
}

fun executeScopedAction(f: (Scope) -> Unit) =
    getKoin()
        .createScope<RequestScope>()
        .also { f(it); it.close() }