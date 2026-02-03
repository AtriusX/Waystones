package xyz.atrius.waystones.service

import org.bukkit.Material
import org.bukkit.block.Bed
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import org.bukkit.block.ShulkerBox
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.type.ChiseledBookshelf
import org.bukkit.block.data.type.Door
import org.bukkit.block.data.type.Gate
import org.bukkit.block.data.type.HangingSign
import org.bukkit.block.data.type.Shelf
import org.bukkit.block.data.type.Sign
import org.bukkit.block.data.type.Switch
import org.bukkit.block.data.type.TrapDoor
import org.bukkit.block.data.type.WallHangingSign
import org.bukkit.block.data.type.WallSign
import org.bukkit.entity.Player
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory

@Single
class WarpValidationService(
    private val waystoneService: WaystoneService,
) {
    // We don't have an easy way to check if a player is clicking on  a block that opens a GUI,
    // so we maintain a list of blocks that open a GUI when interacted with.
    private val guiBlocks = setOf(
        Material.ANVIL,
        Material.BARREL,
        Material.BEACON,
        Material.CHIPPED_ANVIL,
        Material.CRAFTING_TABLE,
        Material.BLAST_FURNACE,
        Material.BREWING_STAND,
        Material.CARTOGRAPHY_TABLE,
        Material.CHEST,
        Material.COMMAND_BLOCK,
        Material.CHAIN_COMMAND_BLOCK,
        Material.REPEATING_COMMAND_BLOCK,
        Material.COPPER_CHEST,
        Material.CRAFTER,
        Material.CRAFTING_TABLE,
        Material.DAMAGED_ANVIL,
        Material.DISPENSER,
        Material.DROPPER,
        Material.ENCHANTING_TABLE,
        Material.ENDER_CHEST,
        Material.FURNACE,
        Material.GRINDSTONE,
        Material.HOPPER,
        Material.LECTERN,
        Material.LOOM,
        Material.SMITHING_TABLE,
        Material.SMOKER,
        Material.STONECUTTER,
        Material.STRUCTURE_BLOCK,
        Material.TEST_BLOCK,
        Material.TEST_INSTANCE_BLOCK,
        Material.TRAPPED_CHEST,
    )

    // There are some blocks which do not have a GUI but are still interactable and
    // may interfere with warping if the player is trying to interact.
    private val interactableBlocks = setOf(
        Material.BELL,
        Material.COMPARATOR,
        Material.DAYLIGHT_DETECTOR,
        Material.DECORATED_POT,
        Material.LEVER,
        Material.NOTE_BLOCK,
        Material.REPEATER,
    )

    fun validateCanWarp(player: Player, block: Block?): Boolean {
        // Players shouldn't be able to warp while gliding using an elytra, as they would be constantly moving
        if (player.isGliding) {
            logValidationFailure(player, "Player is gliding with elytra")
            return false
        }
        // Don't allow warp if we are clicking on a waystone, as that would interfere with linking
        if (waystoneService.isWaystone(block)) {
            return false
        }
        // Check block state
        if (!validateBlockState(player, block?.state)) {
            return false
        }
        // Check block data
        if (!validateBlockData(player, block?.blockData)) {
            return false
        }

        if (!validateBlockType(player, block)) {
            return false
        }

        logger.debug("Warp validation succeeded for ${player.name}")
        return true
    }

    private fun validateBlockData(player: Player, data: BlockData?): Boolean = when (data) {
        // Check if block is any sign type
        is Sign, is HangingSign, is WallSign, is WallHangingSign -> {
            logValidationFailure(player, "Player is interacting with a sign")
            false
        }
        // Check if block is any door type
        is Door, is TrapDoor, is Gate -> {
            logValidationFailure(player, "Player is interacting with a door, trapdoor, or gate")
            false
        }
        // This should primarily catch levers and buttons
        is Switch -> {
            logValidationFailure(player, "Player is interacting with a switch or button")
            false
        }

        is Shelf -> {
            logValidationFailure(player, "Player is interacting with a shelf")
            false
        }

        is ChiseledBookshelf -> {
            logValidationFailure(player, "Player is interacting with a chiseled bookshelf")
            false
        }

        else -> true
    }

    private fun validateBlockState(player: Player, state: BlockState?): Boolean = when (state) {
        is Bed -> {
            logValidationFailure(player, "Player is on a bed")
            false
        }

        is ShulkerBox -> {
            logValidationFailure(player, "Player is viewing the contents of a shulker box")
            false
        }

        else -> true
    }

    private fun validateBlockType(player: Player, block: Block?): Boolean = when (block?.type) {
        // Don't warp if an interface is open. This prevents the player from accidentally warping when interacting with
        // a block that has a GUI screen. This prioritizes the player's intent to interact with the GUI over warping.
        in guiBlocks -> {
            logValidationFailure(player, "Player is viewing GUI of ${block?.type}")
            false
        }
        // Don't warp if the player is interacting with an interactable block that we track
        in interactableBlocks -> {
            logValidationFailure(player, "Player is interacting with ${block?.type}")
            false
        }

        else -> true
    }

    private fun logValidationFailure(player: Player, message: String) {
        logger.debug("Warp validation failed for ${player.name}: $message")
    }

    companion object {

        private val logger = LoggerFactory
            .getLogger(WarpValidationService::class.java)
    }
}
