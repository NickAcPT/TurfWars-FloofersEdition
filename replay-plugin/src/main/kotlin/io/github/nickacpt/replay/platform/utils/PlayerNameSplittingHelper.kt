package io.github.nickacpt.replay.platform.utils

/**
 * Helper class to split player names
 */
object PlayerNameSplittingHelper {
    private const val maxNameSize = 16
    private const val npcNameSuffix = "§r"
    private const val npcMaxNameSize = maxNameSize - npcNameSuffix.length

    /**
     * Split player name into two parts: name and rest.
     *
     * The name section cannot be longer than [maxNameSize] characters, if it is, it will be split at the [maxNameSize] character mark.
     * If the name section is shorter than [maxNameSize], [npcNameSuffix] will be added at the end of name.
     *
     * @param name The player name to split
     * @return A pair containing the split name and rest
     */
    fun splitName(name: String): PlayerNameSplitData {
        val finalName = StringBuilder(name).apply {
            insert(name.length.coerceAtMost(npcMaxNameSize), npcNameSuffix)
        }.toString()

        return PlayerNameSplitData(finalName.take(maxNameSize), finalName.drop(maxNameSize))
    }
}