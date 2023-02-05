package io.github.nickacpt.event.core.metrics

import io.github.nickacpt.event.core.players.TurfPlayerData
import io.github.nickacpt.event.database.DatabaseObjectConverter
import io.github.nickacpt.event.utils.get
import java.sql.ResultSet

data class MetricLeaderboardEntry<T>(
    val player: TurfPlayerData,
    val value: T
) {
    companion object : DatabaseObjectConverter<MetricLeaderboardEntry<*>> {
        override fun createObjectFromDatabase(rs: ResultSet): MetricLeaderboardEntry<*> {
            return MetricLeaderboardEntry<Int>(
                TurfPlayerData(rs["player_id"]!!),
                rs["value"]!!
            )
        }
    }
}