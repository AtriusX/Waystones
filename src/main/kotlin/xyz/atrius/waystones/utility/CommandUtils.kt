package xyz.atrius.waystones.utility

import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack

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
