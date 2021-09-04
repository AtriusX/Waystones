package xyz.atrius.waystones.utility

import org.bukkit.ChatColor
import org.bukkit.NamespacedKey
import xyz.atrius.waystones.plugin

// Translate ChatColors using custom ColorCode
fun String.translateColors(colorCode: Char = '&') = ChatColor.translateAlternateColorCodes(colorCode, this)

// Convert string to a namespace key
fun String.toKey() = NamespacedKey(plugin, this)

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

fun Boolean.toInt(): Int = if (this) 1 else 0