package xyz.atrius.waystones.command.waystones

import org.koin.core.annotation.Single
import xyz.atrius.waystones.command.BaseCommand

@Single
class WaystoneCommand(
    override val subCommands: List<WaystoneSubcommand>
) : BaseCommand<WaystoneSubcommand>("ws")
