import com.slaycard.basic.*
import com.slaycard.basic.Result
import com.slaycard.basic.domain.Entity
import com.slaycard.entities.shared.AuctionItemId
import com.slaycard.entities.shared.Money
import com.slaycard.entities.shared.UserId
import com.slaycard.entities.events.AuctionCancelledEvent
import com.slaycard.entities.events.AuctionFinishedEvent
import com.slaycard.entities.events.AuctionPriceOutbidEvent
import com.slaycard.entities.events.AuctionStartedEvent
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

typealias PropertyList = List<Pair<String, String>>

class Auction(
    id: AuctionId = AuctionId(uuid64()),                            // Required Here
    val sellingUser: UserId = UserId(uuid64()),                     // Required Here
    val auctionItemName: String = "Default Item",
    val auctionItemId: AuctionItemId = AuctionItemId(uuid64()),
    val quantity: Int = 1,
    val startingPrice: Money = Money(100),                          // Required Here
    val originalDurationHours: Int = 72,                            // Required Here
    val description: String = "",                                   // Modifiable
    val properties: PropertyList = emptyList(),                     // Modifiable
    timeNow: LocalDateTime = getUtcTimeNow())
    : Entity<AuctionId>(id) {

    init {
        events.add(
            AuctionStartedEvent(
                id, sellingUser, auctionItemName, startingPrice, quantity, description, properties, timeNow, originalDurationHours))
    }

    fun validate(): Result =
        resultActionOfT { result ->
            if (auctionItemName.isEmpty())
                result.fail("The name must contain characters")

            if (!startingPrice.isValid())
                result.fail("The starting price must be greater than zero")
        }

    var currentPrice: Money = startingPrice
        get() = field
        private set (value) { field = value }

    val startTime: LocalDateTime = timeNow
    val endTime: LocalDateTime get() =
        when (cancelTime == null) {
            true -> startTime + DateTimePeriod(hours = originalDurationHours)
            false -> cancelTime as LocalDateTime
        }

    private var cancelTime: LocalDateTime? = null
    var lastBiddingUser: UserId? = null
        private set

    fun isFinished(timeNow: LocalDateTime): Boolean = timeNow >= endTime
    fun isCancelled(): Boolean = cancelTime != null

    fun outbid(money: Money, user: UserId, timeNow: LocalDateTime = getUtcTimeNow()): Result {
        if (isFinished(timeNow))
            return failure("Can't outbid - The auction has finished")

        if (money.value < currentPrice.value * 1.05)
            return failure("The new price must be greater than previous by 5% or more")

        if (lastBiddingUser != null && user == lastBiddingUser)
            return failure("Can't outbid if already has outbid by the same user")

        if (user == sellingUser)
            return failure("Can't bid own auction")

        currentPrice = money
        lastBiddingUser = user

        events.add(AuctionPriceOutbidEvent(id, auctionItemName, user, money))

        return success()
    }

    fun cancel(timeNow: LocalDateTime = getUtcTimeNow()): Result {
        if (isCancelled())
            return failure("Can't cancel already cancelled auction")

        if (isFinished(timeNow))
            return failure("Can't cancel already finished auction")

        cancelTime = timeNow

        events.add(AuctionCancelledEvent(id, auctionItemName, currentPrice, timeNow))

        return success()
    }

    fun finish(timeNow: LocalDateTime = getUtcTimeNow()): Result {
        if (isCancelled())
            return failure("Can't finish the auction because it was cancelled")

        if (isFinished(timeNow))
            return failure("Can't finish the auction before end time")

        events.add(AuctionFinishedEvent(id, auctionItemName, currentPrice, lastBiddingUser, timeNow))

        return success()
    }
}

@Serializable
data class AuctionId(val value: String)
