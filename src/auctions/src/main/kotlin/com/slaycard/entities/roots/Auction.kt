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
    id: AuctionId,                                      // Required Here
    val sellingUserId: UserId,                            // Required Here
    val auctionItemName: String,
    val auctionItemId: AuctionItemId,
    val quantity: Int,
    val startingPrice: Money,                           // Required Here
    val originalDurationHours: Int,                     // Required Here
    val description: String,                            // Modifiable
    val properties: PropertyList,                       // Modifiable
    currentPrice: Money? = null,
    startTime: LocalDateTime? = null,
    timeNow: LocalDateTime = getUtcTimeNow())
    : Entity<AuctionId>(id) {

    init {
        events.add(
            AuctionStartedEvent(
                id, sellingUserId, auctionItemName, startingPrice, quantity, description, properties, timeNow, originalDurationHours))
    }

    var currentPrice: Money = currentPrice ?: startingPrice
        private set

    val startTime: LocalDateTime = startTime ?: timeNow

    fun validate(): Result =
        resultActionOfT { result ->
            if (auctionItemName.isEmpty())
                result.fail("The name must contain characters")

            if (!startingPrice.isValid())
                result.fail("The starting price must be greater than zero")
        }

    val endTime: LocalDateTime
        get() = Auction.getEndTime(startTime, originalDurationHours, cancelTime)

    var cancelTime: LocalDateTime? = null
    var lastBiddingUserId: UserId? = null
        private set

    fun isFinished(timeNow: LocalDateTime): Boolean = Auction.isFinished(timeNow, endTime)
    fun isCancelled(): Boolean = cancelTime != null

    fun outbid(money: Money, userId: UserId, timeNow: LocalDateTime = getUtcTimeNow()): Result {
        if (isFinished(timeNow))
            return failure("Can't outbid - The auction has finished")

        if (money.value < currentPrice.value * 1.05)
            return failure("The new price must be greater than previous by 5% or more")

        if (lastBiddingUserId != null && userId == lastBiddingUserId)
            return failure("Can't outbid if already has outbid by the same user")

        if (userId == sellingUserId)
            return failure("Can't bid own auction")

        currentPrice = money
        lastBiddingUserId = userId

        events.add(AuctionPriceOutbidEvent(id, auctionItemName, userId, money))

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

        events.add(AuctionFinishedEvent(id, auctionItemName, currentPrice, lastBiddingUserId, timeNow))

        return success()
    }

    companion object {
        fun isFinished(timeNow: LocalDateTime, endTime: LocalDateTime): Boolean = timeNow >= endTime
        fun getEndTime(startTime: LocalDateTime, durationHours: Int, cancelTime: LocalDateTime?): LocalDateTime =
            when (cancelTime == null) {
                true -> startTime + DateTimePeriod(hours = durationHours)
                false -> cancelTime as LocalDateTime
            }
    }
}

@Serializable
data class AuctionId(val value: String)
