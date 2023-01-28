package io.github.nickacpt.event.turfwars.minigame.kits

enum class TurfWarsKit {
    /**
     * Unrivaled in archery. One hit kills anyone.
     *
     * Receive 1 Wool every 4 seconds. Max 8.
     * Receive 1 Arrow every 8 seconds. Max 2.
     */
    MARKSMAN,

    /**
     * Able to travel onto enemy turf, but you must return to your turf fast, or receive Slownless.
     *
     *
     * Receive 1 Wool every 4 seconds. Max 4.
     * Receive 1 Arrow every 8 seconds. Max 1.
     */
    INFILTRATOR,

    /**
     * Arrows are weaker, but shred through forts.
     *
     * Receive 1 Wool every 4 seconds. Max 6.
     * Receive 1 Arrow every 4 seconds. Max 2.
     *
     * Charge bow to use Barrage.
     */
    SHREDDER,
}