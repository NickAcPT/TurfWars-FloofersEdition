package io.github.nickacpt.event.core.io

import io.github.nickacpt.event.core.players.TurfPlayerData
import io.github.nickacpt.event.core.tags.PlayerTag
import io.github.nickacpt.event.database.DatabaseMethod
import java.util.*

interface DatabasePlayersFunctions {

    @DatabaseMethod("core_get_player_data_by_id")
    fun getPlayerDataById(id: UUID): TurfPlayerData

    @DatabaseMethod("core_update_player_data")
    fun updatePlayerData(id: UUID, tag: PlayerTag?)
}