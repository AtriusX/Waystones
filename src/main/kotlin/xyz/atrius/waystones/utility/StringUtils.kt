package xyz.atrius.waystones.utility

import org.bukkit.ChatColor

// Translate ChatColors using custom ColorCode
fun String.translateColors(colorCode: Char) = ChatColor.translateAlternateColorCodes(colorCode, this)

// Return an 's' depending on input amount
fun String.pluralize(vararg count: Int): String {
    var result = this
    count.forEach {
        result = result.replaceFirst("(s)", if (it == 1) "" else "s")
    }
    return result
}