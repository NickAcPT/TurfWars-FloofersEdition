package io.github.nickacpt.event.core.metrics

import io.github.nickacpt.event.core.CorePlugin
import io.github.nickacpt.event.core.io.DatabaseMetricsFunctions
import io.github.nickacpt.event.core.players.TurfPlayer
import io.github.nickacpt.event.utils.getDatabaseProxy
import org.bukkit.Bukkit
import java.util.concurrent.ConcurrentLinkedQueue

object PlayerMetricsManager {

    private val metricsIo by lazy { getDatabaseProxy<DatabaseMetricsFunctions>() }

    val ARM_SWINGS by lazy { metricsIo.getMetric("Arm Swings") }

    val BLOCKS_BROKEN by lazy { metricsIo.getMetric("Blocks Broken") }

    val BLOCKS_PLACED by lazy { metricsIo.getMetric("Blocks Placed") }

    private val metricQueue = ConcurrentLinkedQueue<MetricData>()

    data class MetricData(val player: TurfPlayer, val metric: Metric<Int>, val increment: Int)

    internal fun init() {
        // First, we compact all existing metrics
        // This is done synchronously as the plugin is loading
        metricsIo.compactPlayerMetrics()

        // Then, we start the async task that will update the metrics
        Bukkit.getScheduler().runTaskTimerAsynchronously(CorePlugin.instance, Runnable {
            updateMetrics()
        }, 0, 20 /* 1 second */)
    }

    private fun updateMetrics() {
        while (metricQueue.isNotEmpty()) {
            val metricData = metricQueue.poll() ?: break
            metricsIo.insertPlayerMetric(metricData.player.data, metricData.metric, metricData.increment)
        }
    }


    fun TurfPlayer.incrementMetric(increment: Int = 1, metric: PlayerMetricsManager.() -> Metric<Int>) {
        metricQueue.add(MetricData(this, metric(this@PlayerMetricsManager), increment))
    }
}