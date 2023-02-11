package io.github.nickacpt.event.utils.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object CoroutineUtils {

    val scope by lazy { CoroutineScope(MinecraftSchedulerDispatchers.SYNC) }

    fun launchSync(block: suspend CoroutineScope.() -> Unit) {
        scope.launch(context = MinecraftSchedulerDispatchers.SYNC, block = block)
    }

    fun launchAsync(block: suspend CoroutineScope.() -> Unit) {
        scope.launch(
            context = MinecraftSchedulerDispatchers.ASYNC,
            block = block
        )
    }

}

