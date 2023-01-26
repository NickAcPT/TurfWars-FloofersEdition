package io.github.nickacpt.event.turfwars.minigame

enum class MinigameState(val description: String) {
    INITIALIZING("<gray>Initializing.."),
    WAITING("<yellow>Waiting for players.."),
    STARTING("<green>Starting in <yellow><time></yellow>.."),
    TEAM_SELECTION("<gray>Team selection"),
    IN_GAME("<gold>Game in progress"),
    ENDING("<red>Ending");

    /**
     * Whether players can join the game in this state
     */
    fun isWaitingForPlayers(): Boolean {
        return this == WAITING || this == STARTING
    }

    fun isInGame(): Boolean {
        return this == TEAM_SELECTION || this == IN_GAME
    }

    fun showsStateInScoreboard(): Boolean {
        return isWaitingForPlayers() || this == ENDING
    }

}