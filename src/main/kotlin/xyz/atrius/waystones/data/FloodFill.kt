package xyz.atrius.waystones.data

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import xyz.atrius.waystones.data.struct.SizedHashSet
import xyz.atrius.waystones.data.struct.StackSet
import xyz.atrius.waystones.utility.neighbors

class FloodFill(
    private val startPosition: Location,
    maxFill: Int = Int.MAX_VALUE,
    private vararg val materials: Material
) : SizedHashSet<Block>(maxFill) {
    var breakdown: StackSet<Material>

    init {
        fill()
        breakdown = StackSet(toList().map { it.type })
    }

    private tailrec fun fill(blocks: Set<Block> = setOf(startPosition.block)) {
        if (blocks.isEmpty())
            return
        val next = mutableSetOf<Block>()
        for (block in blocks) {
            // Skip the current block if it already exists in the array
            if (block.type in materials && !add(block))
                continue
            // Store the neighbors of the current block in the next set
            for (n in block.location.neighbors) {
                val neighbor = n.world?.getBlockAt(n) ?: continue
                if (neighbor.type in materials)
                    next.add(neighbor)
            }
        }
        // Process the next set of blocks
        fill(next)
    }
}