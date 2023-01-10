package io.github.nickacpt.event.core.display.providers

import io.github.nickacpt.event.core.display.PlayerDisplayData
import io.github.nickacpt.event.core.display.PlayerDisplayProvider
import io.github.nickacpt.event.core.players.TurfPlayer

object PlayerTagProvider : PlayerDisplayProvider {
    override fun provideDisplay(player: TurfPlayer): PlayerDisplayData {
        return PlayerDisplayData(prefix = player.currentTag.value)
    }
}