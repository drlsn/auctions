package com.slaycard

import com.slaycard.plugins.configureMonitoring
import com.slaycard.plugins.configureRouting
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureMonitoring()
    configureRouting()
}
