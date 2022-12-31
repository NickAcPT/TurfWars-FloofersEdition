package io.github.nickacpt.event.core.utils

inline fun <reified T> getConfigurationFileProvider(): ConfigurationFileProvider<T> =
    ConfigurationFileProvider(T::class.java)