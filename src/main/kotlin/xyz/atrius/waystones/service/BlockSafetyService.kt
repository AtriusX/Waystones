package xyz.atrius.waystones.service

import org.bukkit.Location
import org.bukkit.Material
import org.koin.core.annotation.Single
import xyz.atrius.waystones.data.config.property.SafeLiquidsProperty
import xyz.atrius.waystones.data.config.property.type.SafeLiquids
import xyz.atrius.waystones.utility.UP

@Single
class BlockSafetyService(
    private val safeLiquids: SafeLiquidsProperty,
) {
    // Blocks that are non-collidable but still hazardous to teleport into.
    // These deal damage or apply harmful effects to entities occupying their space.
    private val hazardBlocks = setOf(
        Material.FIRE,
        Material.SOUL_FIRE,
        Material.CAMPFIRE,
        Material.SOUL_CAMPFIRE,
        Material.SWEET_BERRY_BUSH,
        Material.WITHER_ROSE,
    )

    fun isLocationSafe(waystone: Location): Boolean {
        val feet = waystone.UP
        val head = feet.UP
        // Check the two blocks above the waystone (head and feet clearance).
        // Uses isCollidable which reflects actual entity collision geometry,
        // unlike isSolid which incorrectly marks signs/pressure plates as solid.
        return checkBlock(feet) &&
            checkBlock(head)
    }

    private fun checkBlock(checkLocation: Location): Boolean {
        val block = checkLocation.world
            ?.getBlockAt(checkLocation)
            ?: return false
        val material = block.type
        // Physical obstruction check: catches doors, potted plants, fences,
        // redstone components with physical presence, all full blocks,
        // and any blocks that may cause damage to the player
        if (block.isCollidable || material in hazardBlocks) {
            return false
        }
        // Liquid check: configurable via safe-liquids property.
        // Liquids have no collision but can still be dangerous or undesirable to land in.
        return when (material) {
            Material.LAVA -> safeLiquids.value() == SafeLiquids.ALL
            Material.WATER -> safeLiquids.value() != SafeLiquids.NONE
            else -> true
        }
    }
}
