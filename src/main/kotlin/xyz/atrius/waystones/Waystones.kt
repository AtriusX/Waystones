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
import xyz.atrius.waystones.commands.WarpstoneCommand

lateinit var plugin       : KotlinPlugin
lateinit var configuration: Config

@Suppress("unused")
class Waystones : KotlinPlugin() {

    lateinit var names: WarpNameService

    override fun onEnable() {
        plugin        = this
        configuration = Config(this)
        names         = WarpNameService(this)

        val events = server.pluginManager
        events.registerEvents(
                WarpEvent(names),
                NameEvent(names),
                DestroyEvent(names),
                InfoEvent(names),
                LinkEvent(names)
        )
        // Register warp key recipe if enabled
        if (configuration.keyItems) {
            server.addRecipe(ShapedRecipe(keyValue(), defaultWarpKey()).apply {
                shape(" * ", "*x*", " * ")
                setIngredient('*', Material.IRON_INGOT)
                setIngredient('x', Material.REDSTONE_BLOCK)
            })
        }
        // Register Waystones Command
        getCommand("waystones")?.setExecutor(WarpstoneCommand)
        logger.info("Warpstones loaded!")
    }

    override fun onDisable() {
        logger.info("Warpstones disabled!")
    }
}