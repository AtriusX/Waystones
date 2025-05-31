package xyz.atrius.waystones.command.waystones

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.ArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.entity.Player
import org.koin.core.annotation.Single
import xyz.atrius.waystones.internal.KotlinPlugin
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.utility.translateColors

@Single
class InfoCommand(
    plugin: KotlinPlugin,
    private val localization: LocalizationManager,
) : WaystoneSubcommand {

    private val pluginDescription = plugin.description
    private val version = pluginDescription.version
    private val author = pluginDescription.authors
        .firstOrNull()
        ?: "Unknown"
    private val contributors = pluginDescription.authors
        .drop(1)
        .joinToString(", ") { "&d$it&r" }
    private val website = pluginDescription.website ?: "N/A"

    private fun infoMsg(player: Player?) = """
        |${localization["plugin-header"].format(player)}
        |Version&7: &a$version&r
        |Author&7: &d$author&r
        |Contributors: $contributors
        |Website: &d&n$website&r
        |${localization["plugin-footer"].format(player)}
    """
        .trimMargin()
        .translateColors()

    override val name: String = "info"

    override fun build(base: ArgumentBuilder<CommandSourceStack, *>) = base
        .executes {
            val sender = it.source.sender

            sender.sendPlainMessage(infoMsg(sender as? Player))
            Command.SINGLE_SUCCESS
        }
}
