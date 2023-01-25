package io.github.nickacpt.event.utils.messages

import io.github.nickacpt.event.utils.messages.annotations.ActionBar
import io.github.nickacpt.event.utils.messages.annotations.Receiver
import net.kyori.adventure.audience.Audience
import net.kyori.moonshine.receiver.IReceiverLocator
import java.lang.reflect.Method

class AudienceReceiverLocator : IReceiverLocator<Audience> {

    override fun locate(method: Method, proxy: Any?, parameters: Array<out Any?>): Audience {
        // Find audience parameter
        var audience = findAnnotatedParameter<Audience, Receiver>(method, parameters)

        // If we were requested to send an action bar, wrap the audience in a special type
        if (audience != null && method.isAnnotationPresent(ActionBar::class.java)) {
            audience = ActionBarSenderAudience(audience)
        }

        // Return the audience, or an empty one if it was null
        return audience ?: Audience.empty()
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <T, reified A : Annotation> findAnnotatedParameter(
        method: Method,
        parameters: Array<out Any?>
    ): T? {
        val receiverParameterIndex =
            method.parameters.indexOfFirst { it.isAnnotationPresent(A::class.java) }.takeIf { it != -1 }

        return receiverParameterIndex?.let { parameters[it] as? T }
    }
}