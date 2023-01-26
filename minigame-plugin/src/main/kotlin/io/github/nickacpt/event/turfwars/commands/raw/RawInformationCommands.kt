package io.github.nickacpt.event.turfwars.commands.raw

import cloud.commandframework.annotations.CommandMethod
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame.Companion.game
import io.github.nickacpt.event.turfwars.minigame.teams.TurfWarsTeam.Companion.team
import net.kyori.adventure.text.Component
import java.util.*

object RawInformationCommands {
    private val mapper = jacksonObjectMapper()

    @CommandMethod("rawgame")
    fun rawGame(player: TurfPlayer) {
        val result = RawGameResult(player.game?.let { RawGameInformation(player, it) })
        player.sendMessage(Component.text(mapper.writeValueAsString(result)))
    }

    @CommandMethod("rawteam")
    fun rawTeam(player: TurfPlayer) {
        val result = RawTeamResult(player.team?.let { RawTeamInformation(it) })
        player.sendMessage(Component.text(mapper.writeValueAsString(result)))
    }
}