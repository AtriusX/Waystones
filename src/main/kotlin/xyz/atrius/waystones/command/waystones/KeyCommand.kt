package xyz.atrius.waystones.command.waystones

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands.argument
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.Localization
import xyz.atrius.waystones.provider.DefaultKeyProvider
import xyz.atrius.waystones.utility.getArgument
import xyz.atrius.waystones.utility.message

@Single
class KeyCommand(
    private val localization: Localization,
    private val defaultKeyProvider: DefaultKeyProvider,
) : WaystoneSubcommand {

    override val name: String = "key"

    override fun build(base: ArgumentBuilder<CommandSourceStack, *>): ArgumentBuilder<CommandSourceStack, *> {
        val base = base
            .requires { it.sender.hasPermission("waystones.getkey.self") }
            .requires { it.sender is Player }
            .executes {
                val sender = it.source.sender as Player
                command(sender, 1, sender)
            }
        val count = argument("count", IntegerArgumentType.integer())
            .requires { it.sender is Player }
            .executes {
                val sender = it.source.sender as Player
                val amount = it.getArgument<Int>("count")

                command(sender, amount, sender)
            }
        val target =  argument("target", ArgumentTypes.player())
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

    private fun command(sender: CommandSender, amount: Int, target: Player): Int {
        target.inventory.addItem(defaultKeyProvider.getKey(amount))
        sender.message(localization["command-give-key", amount, target.name])
        return Command.SINGLE_SUCCESS
    }
}