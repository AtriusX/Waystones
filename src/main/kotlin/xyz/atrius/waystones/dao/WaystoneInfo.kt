package xyz.atrius.waystones.dao

import org.bukkit.Location
import java.util.UUID

data class WaystoneInfo(
    val worldUid: UUID,
    val x: Int,
    val y: Int,
    val z: Int,
    val name: String? = null,
) {

    companion object {

        fun fromLocation(location: Location, name: String? = null): WaystoneInfo = WaystoneInfo(
            worldUid = location.world.uid,
            x = location.blockX,
            y = location.blockY,
            z = location.blockZ,
            name = name,
        )
    }
}
