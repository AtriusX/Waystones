package xyz.atrius.waystones.command.waystones

import org.koin.core.annotation.Single
import xyz.atrius.waystones.command.BaseCommand
import xyz.atrius.waystones.manager.LocalizationManager

@Single
class WaystoneCommand(
    localization: LocalizationManager,
    override val subCommands: List<WaystoneSubcommand>
) : BaseCommand<WaystoneSubcommand>("ws", localization)
