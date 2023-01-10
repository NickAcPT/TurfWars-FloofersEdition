package io.github.nickacpt.event.core.display

import io.github.nickacpt.event.core.players.TurfPlayer

interface PlayerDisplayProvider {
    fun provideDisplay(player: TurfPlayer): PlayerDisplayData? {
        return null
    }
}