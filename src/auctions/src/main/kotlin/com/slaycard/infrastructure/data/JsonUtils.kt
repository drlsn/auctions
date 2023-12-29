package com.slaycard.infrastructure.data

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.Function
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import org.jetbrains.exposed.sql.vendors.PostgreSQLDialect
import org.jetbrains.exposed.sql.vendors.currentDialect
import org.postgresql.util.PGobject
import kotlin.reflect.KClass

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
