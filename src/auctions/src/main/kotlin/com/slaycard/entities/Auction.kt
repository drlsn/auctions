import com.slaycard.basic.*
import com.slaycard.basic.Result
import com.slaycard.entities.AuctionItemId
import com.slaycard.entities.Money
import com.slaycard.entities.UserId
import com.slaycard.entities.events.AuctionPriceOutbidEvent
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

typealias PropertyList = List<Pair<String, String>>

class Auction(
    id: AuctionId,
    val sellingUser: UserId,
    val name: String,
    val auctionItemId: AuctionItemId,
    val quantity: Int,
    val startingPrice: Money,
    val description: String = "",
    val properties: PropertyList = emptyList(),
    getUtcTimeNow: () -> LocalDateTime = { com.slaycard.basic.getUtcTimeNow() })
    : Entity<AuctionId>(id) {

    var currentPrice: Money = startingPrice
        get() = field
        private set (value) { field = value }

    val startDate: LocalDateTime = getUtcTimeNow()

    fun outbid(money: Money): Result {
        if (money.value < currentPrice.value * 1.05)
            return failure("The new price must be greater than previous by 5% or more")

        currentPrice = money

        events.add(AuctionPriceOutbidEvent(id, sellingUser, money))

        return success()
    }

    fun validate(): Result =
        resultActionOfT { result ->
            if (name.isEmpty())
                result.fail("The name must contain characters")

            if (!startingPrice.isValid())
                result.fail("The starting price must be greater than zero")
        }

    companion object {
        fun createDefault(name: String = "auction-1", startingPrice: Money = Money(100), quantity: Int = 1): Auction =
           Auction(
                AuctionId(uuid64()),
                UserId(uuid64()),
                name,
                AuctionItemId(uuid64()),
                quantity,
                startingPrice)
    }
}

@Serializable
data class AuctionId(val value: String)
