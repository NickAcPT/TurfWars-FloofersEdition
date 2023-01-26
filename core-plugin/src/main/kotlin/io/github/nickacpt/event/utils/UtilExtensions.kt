package io.github.nickacpt.event.utils

inline fun <reified T> getConfigurationFileProvider(): ConfigurationFileProvider<T> =
    ConfigurationFileProvider(T::class.java)

/**
 * Appends the element from all the elements separated using [separator] and using the given [prefix] and [postfix] if supplied.
 *
 * If the collection could be huge, you can specify a non-negative value of [limit], in which case only the first [limit]
 * elements will be appended, followed by the [truncated] element.
 */
fun <T, A : MutableList<T>> Iterable<T>.joinTo(buffer: A, separator: T, prefix: T? = null, postfix: T? = null, limit: Int = -1, truncated: T? = null): A {
    if (prefix != null) buffer.add(prefix)
    var count = 0
    for (element in this) {
        if (++count > 1) buffer.add(separator)
        if (limit < 0 || count <= limit) {
            buffer.add(element)
        } else break
    }
    if (limit in 0 until count && truncated != null) buffer.add(truncated)
    if (postfix != null) buffer.add(postfix)
    return buffer
}