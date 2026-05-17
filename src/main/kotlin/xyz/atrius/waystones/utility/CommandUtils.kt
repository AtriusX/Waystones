package xyz.atrius.waystones.utility

import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.RemoteConsoleCommandSender
import org.bukkit.entity.Player
import xyz.atrius.waystones.manager.LocalizationManager

inline fun <reified T> CommandContext<CommandSourceStack>.getArgument(name: String): T =
    getArgument(name, T::class.java)

inline fun <reified T> CommandContext<CommandSourceStack>.getArguments(name: String): List<T> =
    getArguments(name, T::class.java)

fun <T> CommandContext<CommandSourceStack>.getArguments(
    name: String,
    propertyType: Class<T>,
): List<T> {
    val args = arrayListOf<T>()
    var count = 1

    while (true) {
        val name = "$name-$count"

        try {
            val arg = getArgument(name, propertyType)
            args.add(arg)
            count++
        } catch (_: IllegalArgumentException) {
            return args
        }
    }
}

fun CommandSender.senderTypeName(localization: LocalizationManager): String {
    val player = this as? Player
    val message = when (this) {
        is RemoteConsoleCommandSender -> localization["sender-type-rcon"]
        is ConsoleCommandSender -> localization["sender-type-console"]
        is BlockCommandSender -> localization["sender-type-block"]
        is Player -> localization["sender-type-player"]
        else -> localization["sender-type-unknown"]
    }

    return message.format(player)
}
