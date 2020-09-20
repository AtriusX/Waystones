package xyz.atrius.waystones

import xyz.atrius.waystones.data.Config
import xyz.atrius.waystones.event.InfoEvent
import xyz.atrius.waystones.event.WarpEvent
import xyz.atrius.waystones.event.WarpstoneDestroyEvent
import xyz.atrius.waystones.event.WaystoneNameEvent
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.KotlinPlugin
import xyz.atrius.waystones.utility.registerEvents

@Suppress("unused")
class Waystones : KotlinPlugin() {

    lateinit var names        : WarpNameService
    lateinit var configuration: Config

    override fun onEnable() {
        configuration = Config(this)
        names         = WarpNameService(this)

        val events = server.pluginManager
        events.registerEvents(this,
                WarpEvent(this, names, configuration),
                WaystoneNameEvent(names),
                WarpstoneDestroyEvent(names),
                InfoEvent(names, configuration)
        )
        logger.info("Waystones loaded!")
    }

    override fun onDisable() {
        logger.info("Waystones disabled!")
    }
}