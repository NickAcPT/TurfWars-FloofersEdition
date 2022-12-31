package io.github.nickacpt.event.core.utils.i18n

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.moonshine.placeholder.ConclusionValue
import net.kyori.moonshine.placeholder.ContinuanceValue
import net.kyori.moonshine.placeholder.IPlaceholderResolver
import net.kyori.moonshine.util.Either
import java.lang.reflect.Method
import java.lang.reflect.Type
class SimplePlaceholderResolver<T>(val resolver: (T) -> Component) : IPlaceholderResolver<Audience, T, Component> {
    override fun resolve(
        placeholder: String,
        value: T,
        receiver: Audience?,
        owner: Type?,
        method: Method?,
        parameters: Array<out Any?>?
    ): MutableMap<String, Either<ConclusionValue<out Component>, ContinuanceValue<*>>> {
        return mutableMapOf(
            placeholder to Either.left(ConclusionValue.conclusionValue(resolver(value)))
        )
    }
}