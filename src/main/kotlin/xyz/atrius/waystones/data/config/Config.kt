package xyz.atrius.waystones.data.config

import org.bukkit.Material
import org.bukkit.Material.*
import xyz.atrius.waystones.Power
import xyz.atrius.waystones.Power.INTER_DIMENSION
import xyz.atrius.waystones.data.Property
import xyz.atrius.waystones.internal.KotlinPlugin

class Config(plugin: KotlinPlugin) {
    init {
        plugin.config.options().copyDefaults(true)
        plugin.saveDefaultConfig()
    }

    // The minimum base range a waystone can have
    val baseDistance: Property<Int> =
        Property("base-distance", 100, PositiveValueParser)

    // The maximum amount of blocks a portal block can boost the range of a portal (this is for netherite)
    val maxBoost: Property<Int> =
        Property("max-boost", 50, PositiveValueParser)

    // The max number of blocks allowed in warp structures
    val maxWarpSize: Property<Int> =
        Property("max-warp-size", 25, PositiveValueParser)

    // Determines if/how power is required from a respawn anchor in order to use the warp
    val requirePower: Property<Power> =
        Property("require-power", INTER_DIMENSION, EnumParser(Power::class))

    // How much power is required for a waystone to be used
    val powerCost: Property<Int> =
        Property("power-cost", 1, RangeParser(1 .. 4))

    // Default block list for power blocks
    val blockMappings: Map<Material, () -> Int> = mapOf(
        NETHERITE_BLOCK to ::netheriteBoost,
        EMERALD_BLOCK to ::emeraldBoost,
        DIAMOND_BLOCK to ::diamondBoost,
        GOLD_BLOCK to ::goldBoost,
        COPPER_BLOCK to ::copperBoost,
        IRON_BLOCK to ::ironBoost
    )

    // Default block function mappings
    val defaultBlocks: Array<Material> =
        blockMappings.keys.toTypedArray()

    // Netherite grants the max amount of boost per block
    private fun netheriteBoost(): Int = maxBoost()

    // Emerald grants 75% of the max boost per block
    private fun emeraldBoost(): Int = (maxBoost() * 0.75).toInt()

    // Diamond grants 50% of the max boost per block
    private fun diamondBoost(): Int = maxBoost() / 2

    // Gold grants 33% of the max boost per block
    private fun goldBoost(): Int = maxBoost() / 3

    // Copper grants 25% of the max boost per block
    private fun copperBoost(): Int = maxBoost() / 4

    // Iron grants 20% of the max boost per block
    private fun ironBoost(): Int = maxBoost() / 5
}
