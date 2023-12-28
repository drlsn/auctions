package com.slaycard.infrastructure

import Auction
import AuctionId
import PropertyList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.Function
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.vendors.PostgreSQLDialect
import org.jetbrains.exposed.sql.vendors.currentDialect
import org.postgresql.util.PGobject
import java.util.*
import kotlin.reflect.KClass

class AuctionExposedRepository {

    suspend fun getById(id: AuctionId): Deferred<Auction?> = dbQuery {
        val uuid =
            try { UUID.fromString(id.value) }
            catch (ex: IllegalArgumentException) { return@dbQuery null }

        val row = AuctionBidTable
            .select(AuctionBidTable.id eq uuid)
            .firstOrNull() ?:
            return@dbQuery null

        Auction(id)
    }

    suspend fun add(auction: Auction): Deferred<Boolean> = dbQuery {
        val uuid =
            try { UUID.fromString(auction.id.value) }
            catch (ex: IllegalArgumentException) { return@dbQuery false }

        AuctionBidTable.insert {
            row -> row[id] = uuid
        }

        true
    }

}

object AuctionBidTable : UUIDTable("auctionBids") {

}

object AuctionDescriptionsTable : UUIDTable("auctionDescriptions") {
    val description = varchar("description", 2000)
    val properties = json<PropertyList>(
        "propertyList",
        { Json.encodeToString(it as PropertyList) },
        { Json.decodeFromString(it) as PropertyList })
}

object AuctionEventsTable : UUIDTable("auctionEvents") {

}

suspend fun<TResult> dbQuery(f: () -> TResult): Deferred<TResult> =
    suspendedTransactionAsync(Dispatchers.IO) { f() }

fun <T : Any> Table.json(name: String, serialize: (Any) -> String, deserialize: (String) -> Any): Column<T> =
    registerColumn(name, JsonColumnType(serialize, deserialize))

class JsonColumnType(
    private val serialize: (Any) -> String,
    private val deserialize: (String) -> Any
) : ColumnType() {
    override fun sqlType() = "JSON"

    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        super.setParameter(
            stmt,
            index,
            value.let {
                PGobject().apply {
                    this.type = sqlType()
                    this.value = value as String?
                }
            }
        )
    }

    override fun valueFromDB(value: Any): Any {
        if (value !is PGobject) {
            return value
        }
        return deserialize(checkNotNull(value.value))
    }

    override fun notNullValueToDB(value: Any): String = serialize(value)
}

// -----------------

inline fun <reified T : Any> Column<*>.jsonValue(vararg jsonPath: String): Function<T> =
    this.jsonValue(T::class, *jsonPath)

fun <T : Any> Column<*>.jsonValue(clazz: KClass<T>, vararg jsonPath: String): Function<T> {
    if (this.columnType !is JsonColumnType) {
        throw IllegalArgumentException("Cannot perform jsonValue call on the column which is not related to JsonbColumnType")
    }

    val columnType = when (clazz) {
        Boolean::class -> BooleanColumnType()
        Int::class -> IntegerColumnType()
        Float::class -> FloatColumnType()
        Long::class -> LongColumnType()
        String::class -> TextColumnType()
        else -> TextColumnType()
    }

    return when (currentDialect) {
        is PostgreSQLDialect -> PostgreSQLJsonValue(this, columnType, jsonPath.toList())
        else -> throw NotImplementedError()
    }
}

class PostgreSQLJsonValue<T>(
    private val expr: Expression<*>,
    override val columnType: ColumnType,
    private val jsonPath: List<String>
) : Function<T>(columnType) {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) = queryBuilder {
        append("(")
        append(expr)
        append("${jsonPath.joinToString { it }})::${columnType.sqlType()}")
    }
}
