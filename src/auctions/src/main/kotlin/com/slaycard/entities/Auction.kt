import com.slaycard.basic.*
import java.util.*
import com.slaycard.basic.Result

class Auction(
    id: AuctionId,
    val name: String,
    val auctionItemId: AuctionItemId,
    val quantity: Int,
    val startingPrice: Money,
    val description: String = "",
    val properties: PropertyList = emptyList()) : Entity<AuctionId>(id) {

    var currentPrice: Money = startingPrice
        get() = field
        private set (value) { field = value }

    fun outbid(money: Money): Result {
        if (money.value < currentPrice.value * 1.05)
            return failure("The new price must be greater than previous by 5% or more")

        currentPrice = money

        return success()
    }

    companion object {
        fun create(name: String, startingPrice: Money, quantity: Int = 1): ResultT<Auction> =
            resultActionOfT { result ->
                if (name.isEmpty())
                    result.fail("The name must contain characters")

                if (!startingPrice.isValid())
                    result.fail("The starting price must be greater than zero")

                when (result.isSuccess) {
                    false -> null
                    true -> Auction(
                        AuctionId(UUID.randomUUID().toString()),
                        name,
                        AuctionItemId(UUID.randomUUID().toString()),
                        quantity,
                        startingPrice)
                }
            }
    }
}

data class AuctionId(val value: String)
data class AuctionItemId(val value: String)
typealias PropertyList = List<Pair<String, String>>

data class Money(val value: Int) {
    fun isValid(): Boolean = value >= 0
}

operator fun Money.compareTo(other: Money): Int =
    this.value - other.value