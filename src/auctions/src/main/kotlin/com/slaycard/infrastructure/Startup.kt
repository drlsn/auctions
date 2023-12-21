package com.slaycard.infrastructure

import Auction
import AuctionId
import Money
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseToken
import com.slaycard.api.configureFirebaseAuth
import com.slaycard.api.plugins.configureMonitoring
import com.slaycard.api.plugins.configureRouting
import com.slaycard.basic.Repository
import com.slaycard.application.CreateAuctionCommandHandler
import com.slaycard.application.OutbidAuctionCommandHandler
import com.slaycard.application.GetAuctionQueryHandler
import com.slaycard.application.GetAuctionsQueryHandler
import io.ktor.http.auth.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.module.dsl.scopedOf
import org.koin.core.scope.Scope
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import java.io.InputStream

object FirebaseAdmin {
    private val serviceAccount: InputStream? =
        this::class.java.classLoader.getResourceAsStream("slaycard-auction-firebase-adminsdk.json")

    private val options: FirebaseOptions = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

    fun init(): FirebaseApp = FirebaseApp.initializeApp(options)
}

class FirebaseConfig(name: String?) : AuthenticationProvider.Config(name) {
    internal var authHeader: (ApplicationCall) -> HttpAuthHeader? =
        { call -> call.request.parseAuthorizationHeaderOrNull() }

    var firebaseAuthenticationFunction: AuthenticationFunction<FirebaseToken> = {
        throw NotImplementedError(FirebaseImplementationError)
    }

    fun validate(validate: suspend ApplicationCall.(FirebaseToken) -> User?) {
        firebaseAuthenticationFunction = validate
    }
}

fun ApplicationRequest.parseAuthorizationHeaderOrNull(): HttpAuthHeader? = try {
    parseAuthorizationHeader()
} catch (ex: IllegalArgumentException) {
    println("failed to parse token")
    null
}

private const val FirebaseImplementationError =
    "Firebase  auth validate function is not specified, use firebase { validate { ... } } to fix this"

data class User(val userId: String = "", val displayName: String = "") : Principal

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
        val result = Auction.create("Uriziel's Sword", Money(100))
        if (result.isSuccess && result.value != null)
            repo.add(result.value)
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
