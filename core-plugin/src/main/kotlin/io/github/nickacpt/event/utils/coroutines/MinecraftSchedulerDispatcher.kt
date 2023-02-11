package io.github.nickacpt.event.utils.coroutines

import io.github.nickacpt.event.core.CorePlugin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import org.bukkit.Bukkit
import kotlin.coroutines.CoroutineContext

open class MinecraftSchedulerDispatcher(private val async: Boolean) : CoroutineDispatcher() {

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return true
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(CorePlugin.instance, block)
        } else {
            Bukkit.getScheduler().runTask(CorePlugin.instance, block)
        }
    }
}