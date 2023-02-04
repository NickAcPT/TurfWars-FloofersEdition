package io.github.nickacpt.event.database

interface DatabaseObjectWithId<T> {
    val databaseId: T
}