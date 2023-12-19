
class Auction(
    val id: AuctionId,
    val auctionItemId: AuctionItemId,
    val quantity: Int,
    val originalPrice: Money,
    val name: String,
    val description: String = "",
    val properties: PropertyList = emptyList()) {

    var currentPrice: Money = originalPrice
        get() = field
        private set (value) { field = value }

    fun outbid(money: Money): Boolean {
        if (money <= currentPrice)
            return false

        return true
    }
}

data class AuctionId(val value: String)
data class AuctionItemId(val value: String)
typealias PropertyList = List<Pair<String, String>>

data class Money(val value: Int)

operator fun Money.compareTo(other: Money): Int =
    this.value - other.value