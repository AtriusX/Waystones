package xyz.atrius.waystones.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands.literal

abstract class BaseCommand<S : SubCommand>(
    private val name: String,
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
            .executes {
                for (command in subCommands) {
                    it.source.sender.sendPlainMessage(command.name)
                }

                Command.SINGLE_SUCCESS
            }
            .build()
    }
}
