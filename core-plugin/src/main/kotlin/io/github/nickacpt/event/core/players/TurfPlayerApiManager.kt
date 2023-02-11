package io.github.nickacpt.event.core.players

import io.github.nickacpt.event.core.io.DatabaseApiFunctions
import io.github.nickacpt.event.utils.getDatabaseProxy
import java.util.*

object TurfPlayerApiManager {
    private val apiIo by lazy {
        getDatabaseProxy<DatabaseApiFunctions>()
    }

    suspend fun createNewApiKey(player: TurfPlayerData): UUID {
        return apiIo.createNewApiKey(player)
    }
}