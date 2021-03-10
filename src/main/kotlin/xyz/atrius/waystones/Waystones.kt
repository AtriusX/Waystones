package xyz.atrius.waystones

import xyz.atrius.waystones.commands.*
import xyz.atrius.waystones.data.config.Config
import xyz.atrius.waystones.data.crafting.CompassRecipe
import xyz.atrius.waystones.event.*
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.service.WorldRatioService
import xyz.atrius.waystones.utility.KotlinPlugin
import xyz.atrius.waystones.utility.registerEvents
import xyz.atrius.waystones.utility.registerNamespaces
import xyz.atrius.waystones.utility.registerRecipes

lateinit var plugin       : KotlinPlugin
lateinit var configuration: Config

@Suppress("unused")
class Waystones : KotlinPlugin() {

    override fun onEnable() {
        plugin        = this
        configuration = Config(this)
        // Load services
        WarpNameService.load()
        WorldRatioService.load()
        // Register listeners
        registerEvents(
            WarpEvent,
            NameEvent,
            DestroyEvent,
            InfoEvent,
            LinkEvent
        )
        // Register warp key recipe if enabled
        if (configuration.keyItems()) registerRecipes(
            CompassRecipe
        )
        // Register command namespaces
        registerNamespaces(
            CommandNamespace("waystones").register(
                InfoCommand,
                GetKeyCommand,
                ReloadCommand,
                ConfigCommand
            )
        )
        logger.info("Waystones loaded!")
    }

    override fun onDisable() {
        logger.info("Waystones disabled!")
    }
}