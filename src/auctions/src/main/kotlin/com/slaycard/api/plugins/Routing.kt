package com.slaycard.api.plugins

import com.slaycard.api.contracts.CreateAuctionApiCommand
import com.slaycard.api.contracts.GetAuctionApiQuery
import com.slaycard.api.contracts.GetAuctionsApiQuery
import com.slaycard.application.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

const val AUCTIONS_ROUTE = "/auctions"

fun Application.configureRouting() {
    routing {
        get("$AUCTIONS_ROUTE/{id}") {
            executeQueryHandler<GetAuctionApiQuery, GetAuctionQuery, GetAuctionQuery.AuctionDTO, GetAuctionQueryHandler> {
                GetAuctionQuery(it["id"]!!)
            }
        }

        get(AUCTIONS_ROUTE) {
            executeQueryHandler<GetAuctionsApiQuery, GetAuctionsQuery, GetAuctionsQuery.AuctionsDTO, GetAuctionsQueryHandler> {
                GetAuctionsQuery()
            }
        }

        post(AUCTIONS_ROUTE) {
            executeCommandHandler<CreateAuctionApiCommand, CreateAuctionCommand, CreateAuctionCommandHandler> {
                CreateAuctionCommand(it.name, it.originalPrice)
            }
        }
    }
}
