package io.github.nickacpt.event.utils

import io.github.nickacpt.event.database.Database
import java.sql.ResultSet

inline operator fun <reified T> ResultSet.get(id: String): T? {
    return when (T::class.java) {
        String::class.java -> getString(id) as T
        Boolean::class.java -> getBoolean(id) as T

        Int::class.java -> getInt(id) as T
        Long::class.java -> getLong(id) as T

        Short::class.java -> getShort(id) as T
        Float::class.java -> getFloat(id) as T
        Double::class.java -> getDouble(id) as T

        Byte::class.java -> getByte(id) as T
        ByteArray::class.java -> getBytes(id) as T
        else -> getObject(id, T::class.java)
    }.takeIf { !wasNull() }
}

inline fun <reified T> getDatabaseProxy(): T {
    return Database.getProxyForInterface(T::class.java) as T
}