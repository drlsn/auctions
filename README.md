# Auction API

#### Technologies
&nbsp;&nbsp;&nbsp;&nbsp; Kotlin, Ktor, SQL, PostgreSQL, Firebase Authenication

#### API
| HTTP Method | Endpoint | Description |
| --- | --- | --- | 
| `GET` | `/auctions/{auctionId}` | Get specific auction details |
| `GET` | `/auctions` | Get multiple auctions details |
| `POST` | `/auctions` | Add new auction | 
| `DELETE` | `/auctions/{auctionId}` | Cancel ongoing auction | 
| --- | --- | --- | 
| `POST` | `/auctions/{auctionId}/bids` | Add new bid | 
