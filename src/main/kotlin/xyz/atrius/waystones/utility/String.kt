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
