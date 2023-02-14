package io.github.nickacpt.event.turfwars.commands.game

import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import cloud.commandframework.annotations.ProxiedBy
import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.turfwars.minigame.MinigameState
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame.Companion.game
import io.github.nickacpt.event.turfwars.minigame.teams.TurfWarsTeam
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.*
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.JoinConfiguration.newlines
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

@CommandMethod("turfwars|turf|game")
object GameManagementCommands {

    private inline fun gameCommand(player: TurfPlayer, action: (TurfWarsGame) -> Unit) {
        val game = player.game ?: run {
            player.sendMessage(text("You are not in a game!", NamedTextColor.RED))
            return
        }

        action(game)
    }

    @CommandMethod("start")
    @ProxiedBy("forcestart")
    @CommandPermission("turfwars.game.start")
    fun startGame(player: TurfPlayer) = gameCommand(player) { game ->
        if (!game.state.isWaitingForPlayers()) {
            player.sendMessage(text("The game is already started!", NamedTextColor.RED))
            return@gameCommand
        }

        game.forceStart = true
        player.sendMessage(text("Forced the game to start!", NamedTextColor.YELLOW))
    }

    @CommandMethod("list")
    @ProxiedBy("gamelist")
    @CommandPermission("turfwars.game.list")
    fun listPlayers(player: TurfPlayer) = gameCommand(player) { game ->
        val components = mutableListOf<Component>()

        for (team in game.teams) {
            components += team.toListComponent()
        }

        player.sendMessage(join(JoinConfiguration.separators(newline().append(newline()), null), components))
    }

    fun TurfWarsTeam.toListComponent() = text { builder ->
        builder.append(name())
        builder.append(space())
        builder.append(text("Players: ", NamedTextColor.GRAY))

        builder.append(newline())

        if (players.isEmpty()) {
            builder.append(text("-", NamedTextColor.GRAY))
            builder.append(space())
            builder.append(text("This team is empty...", NamedTextColor.GRAY, TextDecoration.ITALIC))
        } else {
            builder.append(
                join(
                    newlines(),
                    players.map { text("- ", NamedTextColor.GRAY).append(it.getDisplayName()) })
            )
        }
    }

}
