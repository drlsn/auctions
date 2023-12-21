import com.slaycard.basic.*
import com.slaycard.basic.Result
import com.slaycard.basic.domain.Entity
import com.slaycard.entities.AuctionItemId
import com.slaycard.entities.Money
import com.slaycard.entities.UserId
import com.slaycard.entities.events.AuctionPriceOutbidEvent
import com.slaycard.entities.events.AuctionStartedEvent
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

typealias PropertyList = List<Pair<String, String>>

class Auction(
    id: AuctionId,
    val byUser: UserId,
    val name: String,
    val auctionItemId: AuctionItemId,
    val quantity: Int,
    val startingPrice: Money,
    val durationHours: Int = 72,
    val description: String = "",
    val properties: PropertyList = emptyList(),
    timeNow: LocalDateTime = getUtcTimeNow())
    : Entity<AuctionId>(id) {

    init {
        events.add(
            AuctionStartedEvent(
                id, byUser,name, startingPrice, quantity, description, properties, timeNow, durationHours))
    }

    fun validate(): Result =
        resultActionOfT { result ->
            if (name.isEmpty())
                result.fail("The name must contain characters")

            if (!startingPrice.isValid())
                result.fail("The starting price must be greater than zero")
        }

    var currentPrice: Money = startingPrice
        get() = field
        private set (value) { field = value }

    val startTime: LocalDateTime = timeNow
    val endTime: LocalDateTime get() = startTime + DateTimePeriod(hours = durationHours)

    fun isFinished(timeNow: LocalDateTime): Boolean = timeNow >= endTime

    fun outbid(money: Money): Result {
        if (money.value < currentPrice.value * 1.05)
            return failure("The new price must be greater than previous by 5% or more")

        currentPrice = money

        events.add(AuctionPriceOutbidEvent(id, byUser, money))

        return success()
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
