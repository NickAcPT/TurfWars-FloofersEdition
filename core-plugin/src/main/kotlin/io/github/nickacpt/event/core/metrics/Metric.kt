package io.github.nickacpt.event.core.metrics

import io.github.nickacpt.event.database.DatabaseObjectConverter
import io.github.nickacpt.event.database.DatabaseObjectWithId
import io.github.nickacpt.event.utils.get
import java.sql.ResultSet

data class Metric<T>(override val databaseId: Int, val name: String) : DatabaseObjectWithId<Int> {
    companion object : DatabaseObjectConverter<Metric<*>> {
        override fun createObjectFromDatabase(rs: ResultSet): Metric<*> {
            return Metric<Int>(
                rs["id"]!!,
                rs["name"]!!
            )
        }
    }
}