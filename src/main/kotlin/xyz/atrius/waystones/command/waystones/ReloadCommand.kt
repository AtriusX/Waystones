package xyz.atrius.waystones.command.waystones

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.ArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands.literal
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.AdvancementManager
import xyz.atrius.waystones.data.config.NewConfigManager
import xyz.atrius.waystones.localization
import xyz.atrius.waystones.utility.message

@Single
class ReloadCommand(
    private val configManager: NewConfigManager
) : WaystoneSubcommand {

    override val name: String = "reload"

    override fun build(base: ArgumentBuilder<CommandSourceStack, *>): ArgumentBuilder<CommandSourceStack, *> {
        val base = base
            .requires { it.sender.hasPermission("waystones.reload") }
            .executes {
                it.source.sender.message(localization["command-reload-usage"])
                Command.SINGLE_SUCCESS
            }
        val reloadAdvancements = literal("advancements")
            .executes {
                AdvancementManager.reload()
                it.source.sender.message(localization["command-reload-advancements"])
                Command.SINGLE_SUCCESS
            }
        val reloadConfig = literal("config")
            .executes {
                configManager.reload()
                it.source.sender.message(localization["command-reload-config"])
                Command.SINGLE_SUCCESS
            }

        return base
            .then(reloadAdvancements)
            .then(reloadConfig)
    }
}