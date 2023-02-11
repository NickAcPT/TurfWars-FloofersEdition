package io.github.nickacpt.event.database

import com.google.common.reflect.AbstractInvocationHandler
import io.github.nickacpt.event.utils.coroutines.CoroutineUtils
import kotlinx.coroutines.runBlocking
import java.lang.reflect.Method
import java.sql.PreparedStatement
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

internal object DatabaseInvocationHandler : AbstractInvocationHandler() {
    private val connection by lazy { Database.connection }

    private val preparedStatementCache = mutableMapOf<String, String>()

    private fun cleanupMethodName(method: Method): String {
        val schema = if (method.declaringClass.isAnnotationPresent(DatabaseSchema::class.java)) {
            method.declaringClass.getAnnotation(DatabaseSchema::class.java).name
        } else if (method.isAnnotationPresent(DatabaseSchema::class.java)) {
            method.getAnnotation(DatabaseSchema::class.java).name
        } else {
            "public"
        }

        val name = if (method.isAnnotationPresent(DatabaseMethod::class.java)) {
            method.getAnnotation(DatabaseMethod::class.java).name
        } else {
            method.name
        }

        return "$schema.$name"
    }

    private fun filterParameters(method: Method) = method.parameters.filterNot { it.type == Continuation::class.java }

    private fun getPreparedStatementForMethod(method: Method): PreparedStatement {
        val prefix = if (method.returnType == Void.TYPE || method.returnType == Unit::class.java) {
            "call"
        } else {
            "select * from"
        }

        val name = cleanupMethodName(method)

        return connection.prepareStatement(
            preparedStatementCache.getOrPut(name) {
                "$prefix $name(${filterParameters(method).joinToString(",") { "?" }})"
            }
        )
    }

    override fun handleInvocation(proxy: Any, method: Method, args: Array<out Any?>): Any? {
        val statement = getPreparedStatementForMethod(method)

        val parameters = filterParameters(method)
        @Suppress("UNCHECKED_CAST") val continuation = method.parameters.lastOrNull() as? Continuation<Any>

        for (i in parameters.indices) {
            statement.setObject(i + 1, Database.objectToSqlQuery(args[i]))
        }

        val resultFetcher = {
            statement.use {
                if (statement.execute()) {
                    Database.resultSetToObjects(statement.resultSet, method)
                } else {
                    null
                }
            }
        }

        return if (continuation != null) {
            CoroutineUtils.launchAsync {
                runBlocking { resultFetcher() }?.let { continuation.resume(it) }
            }
        } else {
            resultFetcher()
        }
    }
}