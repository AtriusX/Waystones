package xyz.atrius.waystones.manager

import org.bukkit.entity.Player
import xyz.atrius.waystones.utility.translateColors
import java.text.MessageFormat
import java.util.Locale

class LocalizedString(
    private val key: String,
    private val args: Array<*>,
    private val values: Map<Locale, MessageFormat>,
    private val preferredPluginLocale: Locale,
    private val defaultLocale: Locale,
) {
    fun format(player: Player?): String {
        val playerLocale = player?.locale()
        val message = values[playerLocale]
            ?: values[preferredPluginLocale]
            ?: values[defaultLocale]
            ?: error("No message found for key '$key' in searched locales: $preferredPluginLocale, $preferredPluginLocale, $defaultLocale!")

        return message
            .format(args)
            .translateColors()
    }
}