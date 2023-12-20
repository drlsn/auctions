package com.slaycard.api.plugins

import com.slaycard.api.contracts.CreateAuctionApiCommand
import com.slaycard.api.contracts.GetAuctionApiQuery
import com.slaycard.api.contracts.GetAuctionsApiQuery
import com.slaycard.api.contracts.OutbidAuctionApiCommand
import com.slaycard.application.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

const val AUCTIONS_ROUTE = "/auctions"

fun Application.configureRouting() {
    routing {
        get("/auctions/{auctionId}") {
            executeQueryHandler<GetAuctionApiQuery, GetAuctionQuery, GetAuctionQuery.AuctionDTO, GetAuctionQueryHandler> {
                routeParams, queryParams, result ->
                GetAuctionQuery(routeParams["auctionId"]!!)
            }
        }

        get("/auctions") {
            executeQueryHandler<GetAuctionsApiQuery, GetAuctionsQuery, GetAuctionsQuery.AuctionsDTO, GetAuctionsQueryHandler> {
                routeParams, queryParams, result ->
                GetAuctionsQuery()
            }
        }

        post("/auctions") {
            executeCommandHandler<CreateAuctionApiCommand, CreateAuctionCommand, CreateAuctionCommandHandler> {
                routeParams, apiCommand ->
                CreateAuctionCommand(apiCommand.name, apiCommand.originalPrice)
            }
        }

        post("/auctions/{auctionId}/bids") {
            executeCommandHandler<OutbidAuctionApiCommand, OutbidAuctionCommand, OutbidAuctionCommandHandler> {
                routeParams, apiCommand ->
                OutbidAuctionCommand(routeParams["auctionId"]!!, apiCommand.newPrice)
            }
        }
    }
}
