package xyz.atrius.waystones.command.waystones

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands.argument
import io.papermc.paper.command.brigadier.Commands.literal
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.command.CommandSender
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.ConfigProperty
import xyz.atrius.waystones.data.config.ConfigPropertyBase
import xyz.atrius.waystones.data.config.ListConfigProperty
import xyz.atrius.waystones.data.config.NewConfigManager
import xyz.atrius.waystones.localization
import xyz.atrius.waystones.utility.getArguments
import xyz.atrius.waystones.utility.message

@Single
class ConfigCommand(
    private val configManager: NewConfigManager,
) : WaystoneSubcommand {
    override val name: String = "config"

    override fun build(base: ArgumentBuilder<CommandSourceStack, *>): ArgumentBuilder<CommandSourceStack, *> {
        val configCommand = base
            .requires { it.sender.hasPermission("waystones.config") }
            .executes {
                listOptions(it.source.sender)
                Command.SINGLE_SUCCESS
            }

        for ((_, option) in configManager) {

            val configSubcommand = when (option) {
                is ConfigProperty<*> -> option.toPropertySubcommand()
                is ListConfigProperty<*> -> option.toPropertySubcommand()
                else -> continue
            }

            configCommand.then(configSubcommand)
        }

        return configCommand
    }

    private fun <T : Any> ConfigProperty<T>.toPropertySubcommand(): LiteralArgumentBuilder<CommandSourceStack> {
        val base = literal(property)
            .executes {
                viewConfigValue<T>(it.source.sender, property)
                Command.SINGLE_SUCCESS
            }
        val configProperty = argument("value", parser)
            .executes {
                val value = it.getArgument("value", propertyType.java)
                configManager.setProperty(property, value)
                updatedPropertyMessage(it.source.sender, property, value)
                Command.SINGLE_SUCCESS
            }

        return base.then(configProperty)
    }

    private fun <T : Any> ListConfigProperty<T>.toPropertySubcommand(): LiteralArgumentBuilder<CommandSourceStack> {
        val root = literal(property)
            .executes {
                viewConfigValue<T>(it.source.sender, property)
                Command.SINGLE_SUCCESS
            }
        val maxArgs = sizes.max()

        if (maxArgs <= 0) {
            return root
        }

        var tail = argument("value-$maxArgs", parser)

        for (i in maxArgs downTo 1) {
            if (i in sizes) {
                tail.executes {
                    val values = it.getArguments("value", javaClass)
                    configManager.setProperty(property, values)
                    updatedPropertyMessage(it.source.sender, property, value)
                    Command.SINGLE_SUCCESS
                }
            }

            if (i > 1) {
                tail = argument("value-${i - 1}", parser)
                    .then(tail)
            }
        }

        return root.then(tail)
    }

    private fun listOptions(sender: CommandSender) {
        sender.message("&7--------- &dWaystones Config &7----------&r")
        // Display each config property
        for ((prop, value) in configManager) {
            val component = getConfigPropertyMessage(prop, value)
            sender.sendMessage(component)
        }

        sender.message("&7-----------------------------------")
    }

    private fun getConfigPropertyMessage(
        property: String,
        value: ConfigPropertyBase<*, *, *>
    ): Component {
        val propertyInfo = Component
            .text(localization[value.getLocalizedInfoKey()].format())
            .color(TextColor.color(0xFF00FF))
        val hover = HoverEvent.hoverEvent(
            HoverEvent.Action.SHOW_TEXT,
            propertyInfo,
        )
        val value = Component
            .text("${value.value}")
            .color(TextColor.color(0x00FFFF))
        return Component
            .text("$property: ")
            .hoverEvent(hover)
            .append(value)
    }

    private fun updatedPropertyMessage(sender: CommandSender, property: String, value: Any) {
        sender.message(localization["command-config-set", property, value])
    }

    private fun <T : Any> viewConfigValue(sender: CommandSender, property: String) {
        val prop = configManager.getValueOrNull<T>(property)
            ?: configManager.getListValueOrNull<T>(property)
        sender.message(localization["command-config-view", property, prop])
    }
}