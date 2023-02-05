package io.github.nickacpt.event.core.io

import io.github.nickacpt.event.core.metrics.Metric
import io.github.nickacpt.event.core.metrics.MetricLeaderboardEntry
import io.github.nickacpt.event.core.players.TurfPlayerData
import io.github.nickacpt.event.database.DatabaseMethod
import java.util.*

interface DatabaseMetricsFunctions {
    /**
     * Retrieves a metric with the specified name, or creates it if it does not exist.
     *
     * @param metricName The name of the metric to retrieve or create.
     * @return The retrieved or created metric.
     */
    @DatabaseMethod("get_or_create_metric")
    fun getMetric(metricName: String): Metric<Int>

    /**
     * Inserts a player metric value into the database.
     *
     * @param player The player associated with the metric value.
     * @param metric The metric associated with the value.
     * @param value The value to insert.
     */
    @DatabaseMethod("insert_player_metric")
    fun <T> insertPlayerMetric(player: TurfPlayerData, metric: Metric<T>, value: T)

    /**
     * Retrieves a leaderboard of players for a specific metric, ordered by the sum of their values for that metric.
     *
     * @param metric The metric to retrieve a leaderboard for.
     * @return A list of entries representing the top players for the metric.
     */
    @DatabaseMethod("get_top_players")
    fun <T> getTopPlayers(metric: Metric<T>): List<MetricLeaderboardEntry<T>>

    /**
     * Compacts the player metrics by summing all values for each unique player and metric pair, and then deleting the original values.
     */
    @DatabaseMethod("compact_player_metrics")
    fun compactPlayerMetrics()
}