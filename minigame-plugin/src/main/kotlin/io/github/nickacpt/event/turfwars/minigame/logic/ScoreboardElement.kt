package io.github.nickacpt.event.turfwars.minigame.logic

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

sealed interface ScoreboardElement {
    data class GroupScoreboardElement(val title: Component, val valueFetcher: () -> Any) : ScoreboardElement {
        constructor(title: String, valueFetcher: () -> Any) : this(
            Component.text(title, null, TextDecoration.BOLD), valueFetcher
        )

        override fun toComponentList() =
            listOf(title.style(Style.style(TextColor.color(0xa0db41), TextDecoration.BOLD)), valueToComponent())

        private fun valueToComponent(): Component {
            val value = valueFetcher()
            if (value is ComponentLike) {
                return value.asComponent()
            }

            return Component.text(value.toString())
        }
    }

    data class SimpleScoreboardElement(val component: Component) : ScoreboardElement {
        override fun toComponentList() = listOf(component)
    }

    fun toComponentList(): List<Component>
}