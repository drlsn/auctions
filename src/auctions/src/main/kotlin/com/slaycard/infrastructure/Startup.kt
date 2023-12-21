package com.slaycard.infrastructure

import Auction
import AuctionId
import com.slaycard.entities.Money
import com.slaycard.api.plugins.configureMonitoring
import com.slaycard.api.plugins.configureRouting
import com.slaycard.basic.Repository
import com.slaycard.application.CreateAuctionCommandHandler
import com.slaycard.application.OutbidAuctionCommandHandler
import com.slaycard.application.GetAuctionQueryHandler
import com.slaycard.application.GetAuctionsQueryHandler
import io.ktor.serialization.kotlinx.json.*
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
    FirebaseAdmin.init()
    configureFirebaseAuth()

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
        repo.add(Auction.createDefault("Uriziel's Sword", Money(100)))
        repo
    }

    scope<RequestScope> {
        scopedOf(::CreateAuctionCommandHandler)
        scopedOf(::GetAuctionQueryHandler)
        scopedOf(::GetAuctionsQueryHandler)
        scopedOf(::OutbidAuctionCommandHandler)
    }
}

class RequestScope: KoinScopeComponent {
    override val scope: Scope by lazy { createScope(this) }
}
