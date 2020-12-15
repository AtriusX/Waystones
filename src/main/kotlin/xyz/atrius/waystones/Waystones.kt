package xyz.atrius.waystones

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import xyz.atrius.waystones.commands.WarpstoneCommand
import xyz.atrius.waystones.data.config.Config
import xyz.atrius.waystones.event.*
import xyz.atrius.waystones.service.WarpNameService
import xyz.atrius.waystones.utility.KotlinPlugin
import xyz.atrius.waystones.utility.defaultWarpKey
import xyz.atrius.waystones.utility.registerCommands
import xyz.atrius.waystones.utility.registerEvents

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
            server.addRecipe(ShapedRecipe(NamespacedKey(plugin, "is_warp_key"), defaultWarpKey()).apply {
                shape(" * ", "*x*", " * ")
                setIngredient('*', Material.IRON_INGOT)
                setIngredient('x', Material.REDSTONE_BLOCK)
            })
        }
        // Register Waystones Command
        registerCommands(
            "waystones" to WarpstoneCommand
        )
        logger.info("Warpstones loaded!")
    }

    override fun onDisable() {
        logger.info("Warpstones disabled!")
    }
}