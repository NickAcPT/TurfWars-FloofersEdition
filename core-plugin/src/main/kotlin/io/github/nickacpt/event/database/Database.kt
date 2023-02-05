package io.github.nickacpt.event.database

import com.google.common.reflect.Reflection
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.sql.ResultSet
import kotlin.reflect.full.companionObjectInstance

object Database {
    private lateinit var database: HikariDataSource

    val connection get() = database.connection

    internal fun init(url: String, user: String, pwd: String) {
        database = HikariDataSource(HikariConfig().apply {
            jdbcUrl = url
            driverClassName = "org.postgresql.Driver"
            username = user
            password = pwd
        })
    }

    fun getProxyForInterface(clazz: Class<*>): Any {
        return Reflection.newProxy(clazz, DatabaseInvocationHandler)
    }

    internal fun objectToSqlQuery(obj: Any?): Any? {
        if (obj is DatabaseObjectWithId<*>)
            return objectToSqlQuery(obj.databaseId)
        return obj
    }

    private fun getTypeForSingleObject(method: Method): Class<*> {
        val returnType = method.returnType
        val isListReturn = returnType == List::class.java

        return if (isListReturn) {
            ((method.genericReturnType as? ParameterizedType)?.actualTypeArguments?.get(0)) as? Class<*>
                ?: throw Exception("Unable to get type for list return type")
        } else {
            method.returnType
        }
    }

    internal fun resultSetToObjects(resultSet: ResultSet, method: Method): Any? {
        val list = mutableListOf<Any>()
        val returnType = getTypeForSingleObject(method)

        while (resultSet.next()) {
            resultSetToObject(resultSet, returnType)?.let { list.add(it) }
        }

        return if (list.isEmpty()) {
            null
        } else if (list.size == 1) {
            list[0]
        } else {
            list
        }
    }

    private fun resultSetToObject(resultSet: ResultSet, returnType: Class<*>): Any? {
        val isSoloResult = resultSet.metaData.columnCount == 1

        if (isSoloResult) {
            return resultSet.getObject(1, returnType)
        }

        val kClass = returnType.kotlin
        val converter = kClass.companionObjectInstance as? DatabaseObjectConverter<*>
            ?: throw Exception("${returnType.simpleName} doesn't implement DatabaseObjectConverter<${returnType.simpleName}>")

        return returnType.cast(converter.createObjectFromDatabase(resultSet))
    }
}