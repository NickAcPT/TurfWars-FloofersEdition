package io.github.nickacpt.event.core.tags

import io.github.nickacpt.event.database.DatabaseObjectConverter
import io.github.nickacpt.event.database.DatabaseObjectWithId
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage
import java.sql.ResultSet

data class PlayerTag(override val databaseId: Int, val name: String, val value: Component, val adminOnly: Boolean) : DatabaseObjectWithId<Int> {
    companion object : DatabaseObjectConverter<PlayerTag> {
        override fun createObjectFromDatabase(rs: ResultSet): PlayerTag {
            return PlayerTag(
                rs.getInt("id"),
                rs.getString("name"),
                miniMessage().deserialize(rs.getString("component")),
                rs.getBoolean("admin_only")
            )
        }
    }
}