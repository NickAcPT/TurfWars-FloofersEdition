package io.github.nickacpt.event.core.utils.i18n

import io.github.nickacpt.event.core.config.i18n.I18nConfiguration
import io.leangen.geantyref.TypeToken
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.moonshine.Moonshine
import net.kyori.moonshine.receiver.IReceiverLocator
import net.kyori.moonshine.strategy.StandardPlaceholderResolverStrategy
import net.kyori.moonshine.strategy.supertype.StandardSupertypeThenInterfaceSupertypeStrategy
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

fun <T> simplePlaceholderResolver(resolver: (T) -> Component): SimplePlaceholderResolver<T> {
    return SimplePlaceholderResolver(resolver = resolver)
}

inline fun <reified T> JavaPlugin.moonshine(configuration: I18nConfiguration): T {
    return Moonshine.builder<T, Audience>(TypeToken.get(T::class.java))
        .receiverLocatorResolver({ _, _ ->
            IReceiverLocator { _, _, _ ->
                Audience.empty()
            }
        }, 0)
        .sourced { _, messageKey -> configuration.messages.getOrDefault(messageKey, messageKey) }
        .rendered(SimpleMessageRenderer(configuration))
        .sent(Audience::sendMessage)
        .resolvingWithStrategy(StandardPlaceholderResolverStrategy(StandardSupertypeThenInterfaceSupertypeStrategy(false)))
        .weightedPlaceholderResolver(Player::class.java, simplePlaceholderResolver { it.displayName() }, 0)
        .weightedPlaceholderResolver(String::class.java, simplePlaceholderResolver { Component.text(it) }, 0)
        .create(this.javaClass.classLoader)
}