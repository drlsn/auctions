# Auctions API

&nbsp;&nbsp;&nbsp;&nbsp; Auction service which goal is to serve in an internal network of a distributed system.

### Technologies

&nbsp;&nbsp;&nbsp;&nbsp; Kotlin, Ktor, Exposed/PostgreSQL, Firebase Authentication

## API

**Access Roles**
- Guest - no token required
- Any Signed User - token required
- Resource Owner User - token required and must be owner of the resource
- Admin - token required with administrator privileges

&nbsp;&nbsp;&nbsp;&nbsp;  
**Endpoints**

| HTTP Method | Endpoint | Description | Access Role |
| --- | --- | --- | --- | 
| `GET` | `/auctions/{auctionId}` | Get specific auction details | Guest |
| `GET` | `/auctions` | Get multiple auctions details | Guest |
| `POST` | `/auctions` | Add new auction | Any Signed User |
| `DELETE` | `/auctions/{auctionId}` | Cancel ongoing auction | Resource Owner User |
| --- | --- | --- | --- |
| `POST` | `/auctions/{auctionId}/bids` | Add new bid | Any Signed Owner User |

&nbsp;&nbsp;&nbsp;&nbsp;  
## API Details

### Get all users

| HTTP Method | Endpoint | Description | Access Role |
| --- | --- | --- | --- | 
| `GET` | `/auctions/{auctionId}` | Get specific auction details | Guest |

#### Description

Get a list of all users, for administrators only.

#### Success Response

##### *200*
&nbsp;&nbsp;&nbsp;&nbsp; Object containing an array of users

&nbsp;&nbsp;&nbsp;&nbsp; *Fields*
- object
  - value - object
    - id - string
    - sellingUserId - string; uuid format
    - itemId - string; uuid format
    - itemName - string, max 30 characters
    - description - string; max 2000 characters
    - quantity - integer
    - startingPrice - integer
    - currentPrice - integer
    - startTime - dateTime; YYYY-MM-DDThh:mm:ss.msμsns
    - originalDurationHours - integer
    - isFinished - boolean
    - inCancelled - boolean
    - winnerUserId - string; uuid format
    - events

&nbsp;&nbsp;&nbsp;&nbsp; *Example*

```json
{
    "value": {
      "id": "880c35cc-94d2-4177-83d2-69847b7b9ed1",
      "sellingUserId": "c64f1808-108c-4afb-a759-148f8472167d",
      "itemName": "Axe",
      "description": "Very sharp axe",
      "quantity": 1,
      "startingPrice": 100,
      "currentPrice": 100,
      "startTime": "2023-12-31T16:52:28.334451",
      "originalDurationHours": 72,
      "isFinished": false,
      "isCancelled": false,
      "events": [],
      "winnerId": "null",
      "cancelTime": null
  }
}
```


