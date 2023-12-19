package com.slaycard.api.plugins

import com.slaycard.application.AuctionsService
import com.slaycard.infrastructure.RequestScope
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
                    call.respondText(
                        it.get<AuctionsService>().getAll())
                }
            }
        }

        data class CreateAuctionBody(val name: String, val price: Int)
        post(AUCTIONS_ROUTE) {
            executeScopedAction {
                launch {
                    val name = call.parameters["name"]
                    val price = call.parameters["price"]
                    if (name == null || price == null) return@launch
                    call.respond(
                        it.get<AuctionsService>().add(name, price.toInt()))
                }
            }
        }
    }
}

fun executeScopedAction(f: (Scope) -> Unit) =
    getKoin()
        .createScope<RequestScope>()
        .also { f(it); it.close() }