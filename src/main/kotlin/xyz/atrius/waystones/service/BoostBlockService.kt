package xyz.atrius.waystones.service

import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.property.BaseDistanceProperty
import xyz.atrius.waystones.data.config.property.MaxBoostProperty
import xyz.atrius.waystones.data.config.property.MaxWarpSizeProperty

@Single
class BoostBlockService(
    private val baseDistance: BaseDistanceProperty,
    private val maxBoost: MaxBoostProperty,
    private val maxWarpSize: MaxWarpSizeProperty,
) {

    // Default block list for power blocks
    val blockMappings: Map<Material, () -> Int> = mapOf(
        Material.NETHERITE_BLOCK to ::netheriteBoost,
        Material.EMERALD_BLOCK to ::emeraldBoost,
        Material.DIAMOND_BLOCK to ::diamondBoost,
        Material.GOLD_BLOCK to ::goldBoost,
        Material.WAXED_COPPER_BLOCK to ::optimizedCopperBoost,
        Material.COPPER_BLOCK to ::copperBoost,
        Material.WAXED_EXPOSED_COPPER to ::copperBoost,
        Material.EXPOSED_COPPER to ::copperBoost,
        Material.WAXED_WEATHERED_COPPER to ::copperBoost,
        Material.WEATHERED_COPPER to ::copperBoost,
        Material.WAXED_OXIDIZED_COPPER to ::copperBoost,
        Material.OXIDIZED_COPPER to ::copperBoost,
        Material.IRON_BLOCK to ::ironBoost
    )

    // Default block function mappings
    val defaultBlocks: Array<Material> =
        blockMappings.keys.toTypedArray()

    // Returns the max warp distance any waystone can have
    fun maxDistance(): Int =
        baseDistance.value() + maxBoost.value() * maxWarpSize.value()

    // Netherite grants the max amount of boost per block
    private fun netheriteBoost(): Int = maxBoost.value()

    // Emerald grants 75% of the max boost per block
    private fun emeraldBoost(): Int = (maxBoost.value() * 0.75).toInt()

    // Diamond grants 50% of the max boost per block
    private fun diamondBoost(): Int = maxBoost.value() / 2

    // Gold grants 33% of the max boost per block
    private fun goldBoost(): Int = maxBoost.value() / 3

    // A waxed, unoxidized copper block grants 30% of the max boost per block
    private fun optimizedCopperBoost(): Int = (maxBoost.value() * 0.3).toInt()

    // Copper grants 25% of the max boost per block
    private fun copperBoost(): Int = maxBoost.value() / 4

    // Iron grants 20% of the max boost per block
    private fun ironBoost(): Int = maxBoost.value() / 5
}
