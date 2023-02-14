package io.github.nickacpt.event.turfwars.minigame.timer

import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.TextComponent
import kotlin.time.Duration.Companion.seconds

open class CountdownTimer(protected val game: TurfWarsGame, private val totalSeconds: Int) {
    companion object {
        const val TICKS_PER_SECOND = 20
    }

    init {
        require(totalSeconds > 0) { "Total seconds must be greater than 0" }
    }

    val remainingTime get() = realRemainingTime.coerceAtMost(totalSeconds)

    var isRunning = false
        private set
    private var realRemainingTime = totalSeconds + 1
    private var tickCount = 0

    val isFinished: Boolean
        get() = realRemainingTime == 0

    fun restart() {
        reset()
        start()
    }

    fun reset() {
        isRunning = false
        realRemainingTime = totalSeconds + 1
    }

    fun start() {
        isRunning = true
    }

    fun stop() {
        isRunning = false
    }

    fun remainingTime(): Component {
        val timeComponent = remainingTime.seconds.toComponents { minutes, seconds, _ ->
            val timeComponents =
                listOf(minutes, seconds.toLong()).takeIf { minutes > 0 }?.map { it.toString().padStart(2, '0') }
            val components = timeComponents?.map { Component.text(it) }

            components?.let { Component.join(JoinConfiguration.separator(Component.text(":")), it) }
        }

        return timeComponent ?: Component.text("$remainingTime second${if (remainingTime == 1) "" else "s"}")
    }

    open fun onTick(secondsLeft: Int) {}

    open fun onFinish() {}

    /**
     * Ticks the timer, 20 times per second
     */
    fun tick() {
        if (!isRunning || isFinished) return
        if (tickCount-- > 0) return
        tickCount = TICKS_PER_SECOND

        when (--realRemainingTime) {
            0 -> {
                onFinish()
            }

            else -> {
                onTick(realRemainingTime)
            }
        }
    }
}