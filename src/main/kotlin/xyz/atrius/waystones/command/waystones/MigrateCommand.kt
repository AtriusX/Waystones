@file:Suppress("DEPRECATION")

package xyz.atrius.waystones.command.waystones

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.koin.core.annotation.Single
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.message

/**
 * @author Atri
 *
 * This is a temporary subcommand being introduced to give server admins a choice on when they want to migrate their
 * existing waystone name data. There is a necessity for this as the plugin supports two different database types. This
 * gives admins a chance to determine whether they would like to store their data in a MySQL database, as the plugin
 * provides an SQLite configuration out of the box. This will likely be removed at a later point once we've concluded
 * the deprecation period for the json data files.
 */
@Single
@Deprecated("Will be phased out eventually after the warpnames.json deprecation period.")
class MigrateCommand(
    private val warpNameService: WarpNameService,
    private val localization: LocalizationManager,
) : WaystoneSubcommand {
    override val name: String = "migrate"

    override fun build(base: ArgumentBuilder<CommandSourceStack, *>): ArgumentBuilder<CommandSourceStack, *> = base
        .requires { it.sender.isOp }
        .then(
            argument<CommandSourceStack, _>("clear", BoolArgumentType.bool())
                .executes {
                    val source = it.source.sender
                    val clear = it.getArgument("clear", Boolean::class.java)

                    source.message(localization["migrate-waystone-info"])
                    warpNameService.migrate()
                    source.message(localization["migrate-waystone-info-complete"])

                    if (clear) {
                        warpNameService.clearData()
                        source.message(localization["migrate-waystone-clear-data"])
                    }

                    Command.SINGLE_SUCCESS
                }
        )
}
