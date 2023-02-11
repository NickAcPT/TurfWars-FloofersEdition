package io.github.nickacpt.event.utils.coroutines

object MinecraftSchedulerDispatchers {
    val SYNC by lazy { MinecraftSchedulerDispatcher(false) }
    val ASYNC by lazy { MinecraftSchedulerDispatcher(true) }
}