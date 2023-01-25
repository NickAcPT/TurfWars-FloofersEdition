package io.github.nickacpt.event.core.display.scoreboard

import io.github.nickacpt.event.core.players.TurfPlayer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import java.time.LocalDateTime
import java.time.ZoneId

object SidebarManager {

    private val linesNames = NamedTextColor.NAMES.values()
        .map { LegacyComponentSerializer.legacySection().serialize(Component.space().color(it)).replace(" ", "") }

    fun notifySidebarDisplay(target: TurfPlayer, sidebarDisplay: PlayerScoreboardDisplayData) {
        val scoreboard = target.bukkitScoreboard ?: return

        val sidebarObjective =
            scoreboard.getObjective(DisplaySlot.SIDEBAR) ?: scoreboard.registerNewObjective(
                "sidebar",
                Criteria.DUMMY,
                Component.empty()
            ).also { it.displaySlot = DisplaySlot.SIDEBAR }

        sidebarObjective.displayName(sidebarDisplay.title)

        val newFinalLines = arrayOf(
            MiniMessage.miniMessage().deserialize(
                "<gray>    <date:'MM/dd/yyyy'>  ",
                Formatter.date("date", LocalDateTime.now(ZoneId.of("America/New_York"))),
            ),
            Component.empty(),
            *sidebarDisplay.lines.toTypedArray()
        )

        if (newFinalLines.size > sidebarDisplay.trackedLines.size) {
            // We need to add more lines
            for (lineNumber in (sidebarDisplay.trackedLines.size + 1)..newFinalLines.size) {
                startTrackingNewLine(lineNumber, sidebarObjective, sidebarDisplay)
            }
        } else if (sidebarDisplay.trackedLines.size > newFinalLines.size) {
            // We need to remove lines
            for (lineNumber in (newFinalLines.size until sidebarDisplay.trackedLines.size)) {
                sidebarDisplay.trackedLines.pop().stopTracking()
            }
        }

        newFinalLines.forEachIndexed { index, content ->
            sidebarDisplay.trackedLines[index].team.prefix(content)
        }
    }

    private fun startTrackingNewLine(
        lineNumber: Int,
        sidebarObjective: Objective,
        sidebarDisplay: PlayerScoreboardDisplayData
    ) {
        val index = lineNumber - 1
        val entryName = linesNames[index]
        val score = sidebarObjective.getScore(entryName)
        score.score = 15 - index
        val team = sidebarObjective.scoreboard!!.let { scoreboard ->
            val teamName = "sidebar_entry_$index"
            scoreboard.getTeam(teamName) ?: scoreboard.registerNewTeam(teamName).also { it.addEntry(entryName) }
        }

        val line = TrackedScoreboardLine(lineNumber, score, team)
        sidebarDisplay.trackedLines.add(line)
    }
}