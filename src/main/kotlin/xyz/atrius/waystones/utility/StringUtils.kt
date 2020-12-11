package xyz.atrius.waystones.utility

import org.bukkit.ChatColor

fun String.translateColors(colorCode: Char) = ChatColor.translateAlternateColorCodes(colorCode, this)