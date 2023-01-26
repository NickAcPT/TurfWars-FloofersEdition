package io.github.nickacpt.event.turfwars.commands.raw

import io.github.nickacpt.event.turfwars.minigame.MinigameState
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

data class RawMinigameState(
    val name: MinigameState,
    val description: String,
) {
    constructor(state: MinigameState, game: TurfWarsGame) :
            this(state, PlainTextComponentSerializer.plainText().serialize(state.descriptionAsComponent(game)))
}