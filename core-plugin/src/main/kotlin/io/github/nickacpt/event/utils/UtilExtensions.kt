package io.github.nickacpt.event.utils

inline fun <reified T> getConfigurationFileProvider(): ConfigurationFileProvider<T> =
    ConfigurationFileProvider(T::class.java)