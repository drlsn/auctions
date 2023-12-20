package com.slaycard.infrastructure

import Auction
import AuctionId
import AuctionItemId
import Money
import com.slaycard.api.plugins.configureMonitoring
import com.slaycard.api.plugins.configureRouting
import com.slaycard.application.AuctionsService
import com.slaycard.basic.Repository
import io.ktor.serialization.kotlinx.json.*
import io.ktor.serialization.kotlinx.xml.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.module.dsl.scopedOf
import org.koin.core.scope.Scope
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureApp() {
    install(Koin) {
        slf4jLogger()
        modules(auctionsModule)
    }

    install(ContentNegotiation) {
        json()
    }

    configureMonitoring()
    configureRouting()
}

val auctionsModule = module {
    single<Repository<Auction, AuctionId>> {
        val repo = InMemoryRepository<Auction, AuctionId>()
        val auction =
            repo.add(Auction(
                AuctionId("auction-1"),
                AuctionItemId("auction-item-1"),
                quantity = 1,
                originalPrice = Money(100),
                name = "Uriziel's Sword"))
        repo
    }
    scope<RequestScope> {
        scopedOf(::AuctionsService)
    }
}

class RequestScope: KoinScopeComponent {
    override val scope: Scope by lazy { createScope(this) }
}
