package io.github.nickacpt.event.core.players

import io.github.nickacpt.event.core.tags.PlayerTag
import io.github.nickacpt.event.core.tags.TagsManager
import io.github.nickacpt.event.database.DatabaseObjectConverter
import io.github.nickacpt.event.database.DatabaseObjectWithId
import io.github.nickacpt.event.utils.get
import java.sql.ResultSet
import java.util.*

data class TurfPlayerData(val id: UUID, var name: String? = null) : DatabaseObjectWithId<UUID> {
    companion object : DatabaseObjectConverter<TurfPlayerData> {
        override fun createObjectFromDatabase(rs: ResultSet): TurfPlayerData {
            return TurfPlayerData(
                rs["id"]!!,
                rs["name"]!!
            ).apply {
                currentTag = TagsManager.findById(rs["tag"])
            }
        }
    }

    var currentTag: PlayerTag? = null
    override val databaseId: UUID get() = id

}