package xyz.atrius.waystones.utility

import org.bukkit.ChatColor

// Translate ChatColors using custom ColorCode
fun String.translateColors(colorCode: Char) = ChatColor.translateAlternateColorCodes(colorCode, this)

// Return an 's' depending on input amount
fun pluralS(amt: Int): String {
    return if (amt != 1) "s" else ""
}