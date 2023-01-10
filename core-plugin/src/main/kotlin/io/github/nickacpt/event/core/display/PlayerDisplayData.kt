package io.github.nickacpt.event.core.display

import io.github.nickacpt.event.core.players.TurfPlayer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.scoreboard.Scoreboard

data class PlayerDisplayData(val prefix: Component? = null, val suffix: Component? = null) {
    val prefixColor = prefix?.compact()?.color()?.let { NamedTextColor.nearestTo(it) }

    fun applyToScoreboard(receiver: Scoreboard, source: TurfPlayer) {
        val teamName = source.name
        val team = receiver.getTeam(teamName) ?: receiver.registerNewTeam(teamName).also {
            source.bukkitPlayer?.let { p -> it.addEntity(p) }
        }

        team.prefix(prefix?.appendSpace())
        prefixColor?.let { team.color(it) }
        team.suffix(suffix?.let { Component.space().append(it) })
    }
}