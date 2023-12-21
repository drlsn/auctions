# Auction API

&nbsp;&nbsp;&nbsp;&nbsp; Auction service which goal is to serve in an internal network of a distributed system.

### Technologies

&nbsp;&nbsp;&nbsp;&nbsp; Kotlin, Ktor, SQL, PostgreSQL, Firebase Authentication

### API

**Access Roles**
- Guest - no token required
- Any Signed User - token required
- Resource Owner User - token required and must be owner of the resource
- Admin - token required with administrator privileges

| HTTP Method | Endpoint | Description | Access Role |
| --- | --- | --- | --- | 
| `GET` | `/auctions/{auctionId}` | Get specific auction details | Guest |
| `GET` | `/auctions` | Get multiple auctions details | Guest |
| `POST` | `/auctions` | Add new auction | Any Signed User |
| `DELETE` | `/auctions/{auctionId}` | Cancel ongoing auction | Resource Owner User |
| --- | --- | --- | --- |
| `POST` | `/auctions/{auctionId}/bids` | Add new bid | Any Signed Owner User |
