package io.github.nickacpt.event.turfwars.minigame

enum class MinigameState(val description: String) {
    WAITING("<yellow>Waiting for players.."),
    STARTING("<green>Starting in <yellow>%s</yellow> seconds.."),
    IN_GAME("<gold>Game in progress"),
    ENDING("<red>Ending");
}