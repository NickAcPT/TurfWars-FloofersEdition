package io.github.nickacpt.event.lobby.i18n

import io.github.nickacpt.event.utils.messages.annotations.Receiver
import net.kyori.adventure.text.Component
import net.kyori.moonshine.annotation.Message
import net.kyori.moonshine.annotation.Placeholder
import org.bukkit.entity.Player

interface LobbyMessages {
    @Message("player-joined")
    fun playerJoinMessage(@Placeholder("player") player: Player): Component

    @Message("player-left")
    fun playerLeaveMessage(@Placeholder("player") player: Player): Component

    @Message("fell-in-lava")
    fun fellInLava(@Receiver player: Player)

    @Message("parkour-started")
    fun parkourStarted(@Receiver player: Player)

    @Message("parkour-finished")
    fun parkourFinished(@Receiver player: Player)

    @Message("parkour-cancelled")
    fun parkourCancelled(@Receiver player: Player)
}