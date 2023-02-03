package io.github.nickacpt.event.turfwars.commands.raw.model

data class RawGameResult(
    val game: RawGameInformation?,
    val inGame: Boolean = game != null,
)