package com.slaycard.infrastructure

import com.slaycard.application.AuctionsService
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureApp() {
    install(Koin) {
        slf4jLogger()
        modules(auctionsModule)
    }
}

val auctionsModule = module {
    single{ AuctionsService(InMemoryRepository()) }
}
