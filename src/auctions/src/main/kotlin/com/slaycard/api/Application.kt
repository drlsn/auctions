package com.slaycard.api

import com.slaycard.infrastructure.configureApp
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureApp()
}
