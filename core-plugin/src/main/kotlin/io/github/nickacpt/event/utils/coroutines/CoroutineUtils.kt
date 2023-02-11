package io.github.nickacpt.event.utils.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object CoroutineUtils {


    @OptIn(DelicateCoroutinesApi::class)
    fun launchSync(block: suspend CoroutineScope.() -> Unit) {
        GlobalScope.launch(context = MinecraftSchedulerDispatchers.SYNC, block = block)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun launchAsync(block: suspend CoroutineScope.() -> Unit) {
        GlobalScope.launch(
            context = MinecraftSchedulerDispatchers.ASYNC,
            block = block
        )
    }

}

