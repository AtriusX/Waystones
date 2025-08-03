package xyz.atrius.waystones.utility

import org.bukkit.ChatColor
import org.bukkit.NamespacedKey
import xyz.atrius.waystones.internal.KotlinPlugin
import java.util.Locale
import kotlin.enums.enumEntries

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

/**
 * Performs a binding on the value of a string to an enum instance.
 *
 * @receiver The string to bind.
 * @param T The enum type to bind our receiver to.
 * @return The associated enum entry for the provided enum type.
 */
inline fun <reified T : Enum<T>> String?.bindAsEnum(): T {
    val entries = enumEntries<T>()
    val entry = entries
        .firstOrNull { it.toString() == this || it.name == this }
        ?: throw IllegalArgumentException(
            "Unsupported value for ${T::class.simpleName}! Must be one of: ${entries.joinToString()}}"
        )

    return entry
}
