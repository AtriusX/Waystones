package xyz.atrius.waystones.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands.literal
import org.bukkit.entity.Player
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.utility.message

abstract class BaseCommand<S : SubCommand>(
    private val name: String,
    private val localization: LocalizationManager,
) {

    abstract val subCommands: List<S>

    fun create(): LiteralCommandNode<CommandSourceStack> {
        val base = literal(name)

        for (command in subCommands) {
            base.then(
                command.build(
                    literal(command.name)
                )
            )
        }

        return base
            .withSubcommandList()
            .build()
    }

    private fun LiteralArgumentBuilder<CommandSourceStack>.withSubcommandList() = executes {
        val template = "/%s &b%s &7- &d%s"
        val sender = it.source.sender
        val player = sender as? Player

        sender.message(localization["plugin-header"].format(player))

        for (command in subCommands) {
            val permission = command.basePermission

            if (permission != null && !sender.hasPermission(permission)) {
                continue
            }

            val subCommandKey = "$name-${command.name}-subcommand-desc"
            val description = when (subCommandKey in localization) {
                true -> localization[subCommandKey]
                else -> localization["default-subcommand-desc"]
            }

            sender.message(template.format(name, command.name, description.format(player)))
        }

        sender.message(localization["plugin-footer"].format(player))
        Command.SINGLE_SUCCESS
    }
}
