package io.github.nickacpt.event.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

object Database {
    lateinit var database: HikariDataSource
        private set

    internal fun init(url: String, user: String, pwd: String) {
        database = HikariDataSource(HikariConfig().apply {
            jdbcUrl = url
            username = user
            password = pwd
            maximumPoolSize = 6
        })
    }
}