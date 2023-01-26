package io.github.nickacpt.event.turfwars.minigame.timer

import io.github.nickacpt.event.turfwars.minigame.TurfWarsGame

abstract class CountdownTimer(protected val game: TurfWarsGame, private val totalSeconds: Int) {
    companion object {
        const val TICKS_PER_SECOND = 20
    }

    init {
        require(totalSeconds > 0) { "Total seconds must be greater than 0" }
    }

    var isRunning = false
        private set
    var remainingTime = totalSeconds
        private set
    private var tickCount = 0

    val hasFinished: Boolean
        get() = remainingTime == 0

    fun restart() {
        reset()
        start()
    }

    fun reset() {
        isRunning = false
        remainingTime = totalSeconds
    }

    fun start() {
        isRunning = true
    }

    fun stop() {
        isRunning = false
    }

    open fun onTick(secondsLeft: Int) {}

    open fun onFinish() {}

    /**
     * Ticks the timer, 20 times per second
     */
    fun tick() {
        if (!isRunning) return
        if (tickCount-- > 0) return
        tickCount = TICKS_PER_SECOND

        when (remainingTime) {
            0 -> {
                onFinish()
            }

            else -> {
                onTick(remainingTime)

                // Only change the remaining time after the tick
                remainingTime--
            }
        }
    }
}