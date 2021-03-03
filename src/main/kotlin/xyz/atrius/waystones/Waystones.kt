package xyz.atrius.waystones

import xyz.atrius.waystones.commands.CommandNamespace
import xyz.atrius.waystones.commands.GetKeyCommand
import xyz.atrius.waystones.commands.InfoCommand
import xyz.atrius.waystones.commands.ReloadCommand
import xyz.atrius.waystones.data.config.Config
import xyz.atrius.waystones.data.crafting.CompassRecipe
import xyz.atrius.waystones.event.*
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.KotlinPlugin
import xyz.atrius.waystones.utility.registerEvents
import xyz.atrius.waystones.utility.registerNamespaces
import xyz.atrius.waystones.utility.registerRecipes

lateinit var plugin       : KotlinPlugin
lateinit var configuration: Config

@Suppress("unused")
class Waystones : KotlinPlugin() {

    lateinit var names: WarpNameService

    override fun onEnable() {
        plugin        = this
        configuration = Config(this)
        names         = WarpNameService(this)
        // Register listeners
        registerEvents(
            WarpEvent(names),
            NameEvent(names),
            DestroyEvent(names),
            InfoEvent(names),
            LinkEvent(names)
        )
        // Register warp key recipe if enabled
        if (configuration.keyItems()) registerRecipes(
            CompassRecipe
        )
        // Register Waystones Command
//        registerCommands( TODO: Temporary disabling
//            "waystones" to WarpstoneCommand
//        )
        registerNamespaces(
            CommandNamespace("waystones").register(
                InfoCommand,
                GetKeyCommand,
                ReloadCommand
            )
        )
        logger.info("Warpstones loaded!")
    }

    override fun onDisable() {
        logger.info("Warpstones disabled!")
    }
}