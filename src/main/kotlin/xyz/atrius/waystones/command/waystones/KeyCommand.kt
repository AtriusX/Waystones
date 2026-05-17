package xyz.atrius.waystones.command.waystones

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands.argument
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.koin.core.annotation.Single
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.provider.DefaultKeyProvider
import xyz.atrius.waystones.utility.getArgument
import xyz.atrius.waystones.utility.message
import xyz.atrius.waystones.utility.senderTypeName

@Single
class KeyCommand(
    private val localization: LocalizationManager,
    private val defaultKeyProvider: DefaultKeyProvider,
) : WaystoneSubcommand {

    override val name: String = "key"

    override val basePermission: String = "waystones.getkey.self"

    override fun build(base: ArgumentBuilder<CommandSourceStack, *>): ArgumentBuilder<CommandSourceStack, *> {
        val base = base
            .requires { it.sender is Player }
            .executes { executeWithPlayer(it, 1) }
        val count = argument("count", IntegerArgumentType.integer())
            .requires { it.sender is Player }
            .executes {
                val amount = it.getArgument<Int>("count")
                executeWithPlayer(it, amount)
            }
        val target = argument("target", ArgumentTypes.player())
            .requires { it.sender.hasPermission("waystones.getkey.all") }
            .executes {
                val amount = it.getArgument<Int>("count")
                val player = it
                    .getArgument<PlayerSelectorArgumentResolver>("target")
                    .resolve(it.source)
                    .first()

                command(it.source.sender, amount, player)
            }

        count.then(target)
        return base.then(count)
    }

    private fun executeWithPlayer(context: CommandContext<CommandSourceStack>, amount: Int): Int {
        val sender = context.source.sender
        val player = sender as? Player

        if (player == null) {
            sender.message(localization["command-bad-sender", sender.senderTypeName(localization)])
            return Command.SINGLE_SUCCESS
        }

        return command(sender, amount, player)
    }

    private fun command(sender: CommandSender, amount: Int, target: Player): Int {
        target.inventory.addItem(defaultKeyProvider.getKey(target, amount))
        sender.message(localization["command-give-key", amount, target.name])
        return Command.SINGLE_SUCCESS
    }
}
