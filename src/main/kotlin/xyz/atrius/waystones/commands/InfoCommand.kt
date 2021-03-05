package xyz.atrius.waystones.commands

import org.bukkit.command.CommandSender
import xyz.atrius.waystones.plugin
import xyz.atrius.waystones.utility.translateColors

object InfoCommand : SimpleCommand("info", "i") {

    private val desc = plugin.description
    private val version = desc.version
    private val author = desc.authors.getOrNull(0) ?: "Unknown"
    private val contributors = desc.authors.drop(1)
        .joinToString(", ") { "&d$it&r" }
    private val website = desc.website
    private val infoMsg = """
        |
        |&7------------- &dWaystones &7------------&r
        |
        |Version&7: &a$version&r
        |Author&7: &d$author&r
        |Contributors: $contributors
        |Website: &d&n$website&r
        |
        |&7---------------&b-&d-&f-&d-&b-&7---------------
    """.trimMargin().translateColors()

    override fun execute(sender: CommandSender, args: Array<String>) =
        sender.sendMessage(infoMsg)
}