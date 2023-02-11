package io.github.nickacpt.event.core.io

import io.github.nickacpt.event.core.players.TurfPlayerData
import io.github.nickacpt.event.database.DatabaseMethod
import java.util.*

interface DatabaseApiFunctions {

    /**
     * Creates a new API key for the player.
     *
     * @param player The player to create the API key for.
     * @return The API key.
     */
    @DatabaseMethod("api_create_new_api_key")
    suspend fun createNewApiKey(player: TurfPlayerData): UUID
}