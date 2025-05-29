package xyz.atrius.waystones.utility

import org.bukkit.ChatColor
import org.bukkit.NamespacedKey
import xyz.atrius.waystones.internal.KotlinPlugin
import java.util.Locale

// Translate ChatColors using custom ColorCode
fun String.translateColors(colorCode: Char = '&') = ChatColor
    .translateAlternateColorCodes(colorCode, this)

// Convert string to a namespace key
fun String.toKey(plugin: KotlinPlugin? = null) = when (plugin) {
    null -> NamespacedKey("waystones", this)
    else -> NamespacedKey(plugin, this)
}

// Split a multiline string into a spreadable array
fun String.splitMultiline() =
    split("\n").toTypedArray()

// Glues a list together into a single string
fun <T> List<T>.glue(): String = joinToString("")

inline fun <reified T> Collection<T>.split(splitter: (T) -> Boolean): Pair<Array<T>, Array<T>> {
    val match = mutableListOf<T>()
    val rest = mutableListOf<T>()
    for (item in this)
        if (splitter(item)) match += item else rest += item
    return match.toTypedArray() to rest.toTypedArray()
}

operator fun <T> List<T>.contains(values: Set<T>): Boolean =
    values.any(::contains)

private val swap = "[_ ]".toRegex()

fun Any.sanitizedStringFormat(): String = toString()
    .lowercase()
    .replace(swap, "-")

/**
 * Gets a base-level language locale from a specified language locale.
 *
 * @receiver The locale to de-specify.
 * @return The de-specified locale.
 */
fun Locale.despecify(): Locale =
    Locale.of(language)
