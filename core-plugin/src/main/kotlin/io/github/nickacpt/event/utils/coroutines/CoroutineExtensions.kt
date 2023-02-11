package io.github.nickacpt.event.utils.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

suspend inline fun <T> async(noinline block: suspend CoroutineScope.() -> T): T =
    withContext(MinecraftSchedulerDispatchers.ASYNC, block)

suspend inline fun <T> sync(noinline block: suspend CoroutineScope.() -> T): T =
    withContext(MinecraftSchedulerDispatchers.SYNC, block)