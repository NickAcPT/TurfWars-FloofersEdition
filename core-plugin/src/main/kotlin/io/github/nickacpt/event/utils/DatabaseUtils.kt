package io.github.nickacpt.event.utils

import io.github.nickacpt.event.database.Database
import java.sql.ResultSet

inline operator fun <reified T> ResultSet.get(id: String): T? {
    return when (T::class.java) {
        String::class.java -> getString(id) as T
        Int::class.java -> getInt(id) as T
        else -> getObject(id, T::class.java)
    }.takeIf { !wasNull() }
}

inline fun <reified T> getDatabaseProxy(): T {
    return Database.getProxyForInterface(T::class.java) as T
}