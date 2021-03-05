package xyz.atrius.waystones.commands

import org.bukkit.command.CommandSender
import xyz.atrius.waystones.plugin
import xyz.atrius.waystones.utility.message

object InfoCommand : SimpleCommand("info", "i") {

    val desc = plugin.description
    val version = desc.version
    val author = desc.authors.getOrNull(0) ?: "Unknown"
    val contributors = desc.authors.drop(1)
        .joinToString(", ") { "&d$it&r" }
    val infoMsg = """
        |
        |&7------------- &dWaystones &7------------&r
        |
        |Version&7: &a$version&r
        |Author&7: &d$author&r
        |Contributors: $contributors
        |
        |&7---------------&b-&d-&f-&d-&b-&7---------------
    """.trimIndent()

    override fun execute(sender: CommandSender, args: Array<String>) =
        sender.message(infoMsg)
}