package xyz.atrius.waystones.command.waystones

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.ArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.koin.core.annotation.Single
import xyz.atrius.waystones.internal.KotlinPlugin
import xyz.atrius.waystones.utility.translateColors

@Single
class InfoCommand(
    plugin: KotlinPlugin,
) : WaystoneSubcommand {

    private val description = plugin.description
    private val version = description.version
    private val author = description.authors
        .firstOrNull()
        ?: "Unknown"
    private val contributors = description.authors
        .drop(1)
        .joinToString(", ") { "&d$it&r" }
    private val website = description.website ?: "N/A"

    private val infoMsg = """
        |&7------------- &dWaystones &7------------&r
        |
        |Version&7: &a$version&r
        |Author&7: &d$author&r
        |Contributors: $contributors
        |Website: &d&n$website&r
        |
        |&7---------------&b-&d-&f-&d-&b-&7---------------
    """
        .trimMargin()
        .translateColors()

    override val name: String = "info"

    override fun build(base: ArgumentBuilder<CommandSourceStack, *>) = base
        .executes {
            it.source.sender.sendPlainMessage(infoMsg)
            Command.SINGLE_SUCCESS
        }
}
