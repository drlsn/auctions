package com.slaycard.api

import com.slaycard.api.plugins.configureMonitoring
import com.slaycard.api.plugins.configureRouting
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureMonitoring()
    configureRouting()
}

