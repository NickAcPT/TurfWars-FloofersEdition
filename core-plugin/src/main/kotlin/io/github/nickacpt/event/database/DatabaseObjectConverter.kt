package io.github.nickacpt.event.database

import java.sql.ResultSet

interface DatabaseObjectConverter<T> {
    fun createObjectFromDatabase(rs: ResultSet): T
}

