package xyz.atrius.waystones.command.waystones

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands.argument
import io.papermc.paper.command.brigadier.Commands.literal
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.koin.core.annotation.Single
import xyz.atrius.waystones.command.resolver.LimitedArgumentType
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.service.WorldRatioService
import xyz.atrius.waystones.utility.getArgument
import xyz.atrius.waystones.utility.message
import xyz.atrius.waystones.utility.sanitizedStringFormat

@Single
class RatioCommand(
    private val worldRatioService: WorldRatioService,
    private val localization: LocalizationManager,
) : WaystoneSubcommand {

    override val name: String = "ratio"

    override val basePermission: String = "waystones.config"

    override fun build(base: ArgumentBuilder<CommandSourceStack, *>): ArgumentBuilder<CommandSourceStack, *> {
        val base = base
            .executes {
                val sender = it.source.sender
                val player = sender as? Player

                sender.message(localization["plugin-header"].format(player))
                // Display each config property
                for ((world, ratio) in worldRatioService) {
                    if (ratio < 0) {
                        continue
                    }

                    val worldName = world.name.sanitizedStringFormat()
                    val default = worldRatioService.isDefault(world)
                    val message = when (default) {
                        true -> localization["command-ratio-list-default", worldName]
                        else -> localization["command-ratio-list-ratio", worldName, ratio]
                    }

                    sender.message(message)
                }

                sender.message(localization["plugin-footer"].format(player))
                Command.SINGLE_SUCCESS
            }

        return base
            .then(setDefault())
            .then(setCommand())
            .then(removeCommand())
    }

    private fun setCommand(): LiteralArgumentBuilder<CommandSourceStack> {
        val ratio = argument("ratio", DoubleArgumentType.doubleArg(0.0))
            .executes {
                val world = it.getArgument("world", String::class.java)
                val ratio = it.getArgument("ratio", Double::class.java)
                val added = worldRatioService
                    .add(Bukkit.getWorld(world), ratio.toDouble())
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
                    .remove(Bukkit.getWorld(world))
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

    private fun setDefault(): LiteralArgumentBuilder<CommandSourceStack> {
        val world = argument("world", LimitedArgumentType(worlds()))
            .executes {
                val world = it.getArgument<String>("world")

                worldRatioService.default(Bukkit.getWorld(world))
                val message = localization["command-ratio-default-set", world]

                it.source.sender.message(message)
                Command.SINGLE_SUCCESS
            }

        return literal("default")
            .then(world)
    }

    private fun worlds() = Bukkit
        .getWorlds()
        .map { it.name }
        .toTypedArray()
}
