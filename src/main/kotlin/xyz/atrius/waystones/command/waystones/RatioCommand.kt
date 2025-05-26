package xyz.atrius.waystones.command.waystones

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands.argument
import io.papermc.paper.command.brigadier.Commands.literal
import org.bukkit.Bukkit
import org.koin.core.annotation.Single
import xyz.atrius.waystones.command.resolver.LimitedArgumentType
import xyz.atrius.waystones.localization
import xyz.atrius.waystones.service.WorldRatioService
import xyz.atrius.waystones.utility.getArgument
import xyz.atrius.waystones.utility.message

@Single
class RatioCommand(
    private val worldRatioService: WorldRatioService,
) : WaystoneSubcommand {

    override val name: String = "ratio"

    override fun build(base: ArgumentBuilder<CommandSourceStack, *>): ArgumentBuilder<CommandSourceStack, *> {
        val base = base
            .requires { it.sender.hasPermission("waystones.config") }
            .executes {
                val sender = it.source.sender

                sender.message("&7--------- &dWaystones Ratios &7----------&r")
                // Display each config property
                for ((item, ratio) in worldRatioService) {
                    sender.message("&f$item&f: &b$ratio")
                }

                sender.message("&7-----------------------------------")
                Command.SINGLE_SUCCESS
            }

        return base
            .then(setCommand())
            .then(removeCommand())

    }

    private fun setCommand(): LiteralArgumentBuilder<CommandSourceStack> {
        val ratio = argument("ratio", DoubleArgumentType.doubleArg(0.0))
            .executes {
                val world = it.getArgument("world", String::class.java)
                val ratio = it.getArgument("ratio", Double::class.java)
                val added = worldRatioService
                    .add(world, ratio.toDouble())
                val message = when (added) {
                    true -> localization["command-ratio-added-successfully", 0, world]
                    else -> localization["command-ratio-addition-failed", world]
                }

                it.source.sender.message(message)
                Command.SINGLE_SUCCESS
            }
        val world = argument("world", LimitedArgumentType(worlds()))
            .then(ratio)

        return literal("set")
            .then(world)
    }

    private fun removeCommand(): LiteralArgumentBuilder<CommandSourceStack> {
        val world = argument("world", LimitedArgumentType(worlds()))
            .executes {
                val world = it.getArgument<String>("world")
                val removed = worldRatioService
                    .remove(world)
                val message = when (removed) {
                    true -> localization["command-ratio-removed-successfully", 0, world]
                    else -> localization["command-ratio-removal-failed", world]
                }

                it.source.sender.message(message)
                Command.SINGLE_SUCCESS
            }

        return literal("remove")
            .then(world)
    }

    private fun worlds() = Bukkit
        .getWorlds()
        .map { it.name }
        .onEach { println(it) }
        .toTypedArray()
}