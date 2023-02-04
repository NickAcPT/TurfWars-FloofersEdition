package io.github.nickacpt.event.core.config

data class DatabaseConfig(
    val url: String = "jdbc:postgresql://localhost:5432/floofers",
    val username: String = "floofers",
    val password: String = "floofers"
)