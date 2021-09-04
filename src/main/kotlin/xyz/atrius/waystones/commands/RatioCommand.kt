package xyz.atrius.waystones.commands

import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import xyz.atrius.waystones.localization
import xyz.atrius.waystones.service.WorldRatioService
import xyz.atrius.waystones.utility.message
import xyz.atrius.waystones.utility.toInt

object RatioCommand : SimpleCommand("ratio", "ra", "r") {

    override fun execute(sender: CommandSender, args: Array<String>, flags: Flags) {
        if (!sender.hasPermission("waystones.ratios"))
            return sender.message(localization["command-no-permission"])
        if (args.isEmpty())
            return sender.message(localization["command-no-arguments"])
        val environment = setOf("environment", "env", "e") in flags
        val world = (sender as? Player)?.world
        when (args[0]) {
            in setOf("s", "set") -> set(sender, args, world, environment)
            in setOf("r", "rem", "remove") -> remove(sender, args, world, environment)
            in setOf("l", "list") -> list(sender)
            else -> sender.message(localization["command-ratio-invalid-command", args[0]])
        }
    }

    private fun set(sender: CommandSender, args: Array<String>, world: World?, environment: Boolean) {
        if (args.size < 2)
            return sender.message(localization["command-no-arguments"])
        val selection = (args.getOrNull(2)
            ?: if (environment) world?.environment?.name else world?.name)?.replaceFirstChar(Char::uppercase)
        val message = if (args.size > 2)
            localization["command-ratio-world-not-found", environment.toInt(), args[2]]
        else
            localization["command-ratio-bad-sender"]
        val added = WorldRatioService.add(
            selection?.lowercase()
                ?: return sender.message(message),
            environment,
            args[1].toDoubleOrNull()
                ?: return sender.message(localization["command-ratio-bad-ratio", args[1]])
        )
        sender.message(
            if (added)
                localization["command-ratio-added-successfully", environment.toInt(), selection, args[1]]
            else
                localization["command-ratio-addition-failed", selection]
        )
    }

    private fun remove(sender: CommandSender, args: Array<String>, world: World?, environment: Boolean) {
        if (args.size < 2 && sender is ConsoleCommandSender)
            return sender.message(localization["command-no-arguments"])
        val selection = (args.getOrNull(1)
            ?: if (environment) world?.environment?.name else world?.name)?.replaceFirstChar(Char::uppercase)
        val message = if (args.size > 1)
            localization["command-ratio-world-not-found", environment.toInt(), args[1]]
        else
            localization["command-ratio-bad-sender"]
        val added = WorldRatioService.remove(
            selection?.lowercase()
                ?: return sender.message(message),
            environment
        )
        sender.message(
            if (added)
                localization["command-ratio-removed-successfully", environment.toInt(), selection]
            else
                localization["command-ratio-removal-failed", selection]
        )
    }

    private fun list(sender: CommandSender) {
        if (WorldRatioService.isEmpty())
            return sender.message(localization["command-ratio-no-ratios"])
        sender.message("&7--------- &dWaystones Ratios &7----------&r")
        // Display each config property
        for ((item, ratio) in WorldRatioService)
            sender.message("&f$item&f: &b$ratio")
        sender.message("&7-----------------------------------")
    }
}