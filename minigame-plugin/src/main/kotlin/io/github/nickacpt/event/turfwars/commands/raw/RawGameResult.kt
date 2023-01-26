package io.github.nickacpt.event.turfwars.commands.raw

data class RawGameResult(
    val game: RawGameInformation?,
    val inGame: Boolean = game != null,
)