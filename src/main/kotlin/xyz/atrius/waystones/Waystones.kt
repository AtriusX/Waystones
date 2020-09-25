package xyz.atrius.waystones

import org.bukkit.Material
import org.bukkit.inventory.ShapedRecipe
import xyz.atrius.waystones.data.Config
import xyz.atrius.waystones.event.*
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.KotlinPlugin
import xyz.atrius.waystones.utility.defaultWarpKey
import xyz.atrius.waystones.utility.keyValue
import xyz.atrius.waystones.utility.registerEvents

lateinit var configuration: Config

@Suppress("unused")
class Waystones : KotlinPlugin() {

    lateinit var names: WarpNameService

    override fun onEnable() {
        configuration = Config(this)
        names         = WarpNameService(this)

        val events = server.pluginManager
        events.registerEvents(this,
                WarpEvent(this, names, configuration),
                WaystoneNameEvent(names),
                WarpstoneDestroyEvent(names),
                InfoEvent(this, names, configuration),
                LinkEvent(this, names, configuration)
        )
        // Register warp key recipe if enabled
        if (configuration.keyItems) {
            server.addRecipe(ShapedRecipe(keyValue(this), defaultWarpKey(this)).apply {
                shape(" * ", "*x*", " * ")
                setIngredient('*', Material.IRON_INGOT)
                setIngredient('x', Material.REDSTONE_BLOCK)
            })
        }
        logger.info("Warpstones loaded!")
    }

    override fun onDisable() {
        logger.info("Warpstones disabled!")
    }
}