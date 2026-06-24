package xyz.atrius.waystones.dao

import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.UUID

/**
 * Represents a waystone with its location, name, and ownership information.
 *
 * @property worldUid The UUID of the world where the waystone is located
 * @property x The X coordinate of the waystone
 * @property y The Y coordinate of the waystone
 * @property z The Z coordinate of the waystone
 * @property name The display name of the waystone
 * @property waystoneUuid The unique identifier for this waystone (UUID)
 * @property primaryOwnerUuid The UUID of the player who owns this waystone
 * @property isLocked Whether the waystone is locked (only owner can link keys)
 */
data class WaystoneInfo(
    val worldUid: UUID,
    val x: Int,
    val y: Int,
    val z: Int,
    val name: String? = null,
    val waystoneUuid: UUID? = null,
    val primaryOwnerUuid: UUID? = null,
    val isLocked: Boolean = false,
) {

    companion object {

        /**
         * Creates a WaystoneInfo from a location with optional ownership information.
         *
         * @param location The Bukkit location of the waystone
         * @param name Optional display name for the waystone
         * @param owner Optional player who will own this waystone
         * @param isLocked Whether the waystone should be locked by default
         * @return A new WaystoneInfo instance with a generated waystoneUuid
         */
        fun fromLocation(
            location: Location,
            name: String? = null,
            owner: Player? = null,
            isLocked: Boolean = false,
        ): WaystoneInfo = WaystoneInfo(
            worldUid = location.world.uid,
            x = location.blockX,
            y = location.blockY,
            z = location.blockZ,
            name = name,
            waystoneUuid = UUID.randomUUID(),
            primaryOwnerUuid = owner?.uniqueId,
            isLocked = isLocked,
        )
    }
}
