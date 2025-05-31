package xyz.atrius.waystones.manager

import org.bukkit.entity.Player
import xyz.atrius.waystones.utility.despecify
import xyz.atrius.waystones.utility.translateColors
import java.text.MessageFormat
import java.util.Locale

class LocalizedString(
    private val key: String,
    private val args: Array<*>,
    private val values: Map<Locale, MessageFormat>,
    private val fallbackLocale: Locale,
    private val defaultLocale: Locale,
) {

    fun format(player: Player?): String {
        val playerLocale = player?.locale()
        // Attempt to get the player's closest locale
        val playerMessage = when (player) {
            null -> null
            else -> values[playerLocale]
                ?: values[playerLocale?.despecify()]
        }
        // Attempt server-level fallback if the sender is not a player or their locale is not supported
        val message = playerMessage
            ?: values[fallbackLocale]
            ?: values[defaultLocale]
            ?: error(
                "No message found for key '$key' in searched locales: " +
                    "$playerLocale, $fallbackLocale, $defaultLocale!"
            )

        return message
            .format(args)
            .translateColors()
    }
}
