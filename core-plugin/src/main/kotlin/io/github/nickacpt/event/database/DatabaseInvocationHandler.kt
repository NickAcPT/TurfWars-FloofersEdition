package io.github.nickacpt.event.database

import com.google.common.reflect.AbstractInvocationHandler
import java.lang.reflect.Method
import java.sql.PreparedStatement

internal object DatabaseInvocationHandler : AbstractInvocationHandler() {
    private val preparedStatementCache = mutableMapOf<String, String>()

    private fun cleanupMethodName(method: Method): String {
        if (method.isAnnotationPresent(DatabaseMethod::class.java)) {
            return method.getAnnotation(DatabaseMethod::class.java).name
        }

        return method.name
    }

    private fun getPreparedStatementForMethod(method: Method): PreparedStatement {
        val prefix = if (method.returnType == Void.TYPE || method.returnType == Unit::class.java) {
            "call"
        } else {
            "select * from"
        }

        val name = cleanupMethodName(method)

        return Database.connection.prepareStatement(
            preparedStatementCache.getOrPut(name) {
                "$prefix $name(${method.parameters.joinToString(",") { "?" }})"
            }
        )
    }

    override fun handleInvocation(proxy: Any, method: Method, args: Array<out Any?>): Any? {
        val statement = getPreparedStatementForMethod(method)

        for (i in 0 until method.parameterCount) {
            statement.setObject(i + 1, Database.objectToSqlQuery(args[i]))
        }

        return statement.use {
            if (statement.execute()) {
                Database.resultSetToObjects(statement.resultSet, method)
            } else {
                null
            }
        }
    }
}