package xyz.atrius.waystones.test.fixtures

import org.bukkit.Location
import org.bukkit.World
import xyz.atrius.waystones.dao.WaystoneInfo
import java.util.UUID

/**
 * Factory methods for creating test [WaystoneInfo] instances.
 *
 * All parameters have sensible defaults so callers only need to specify
 * the values relevant to their test case.
 */
object WaystoneInfoFixtures {

    fun waystone(
        world: World,
        x: Int = 0,
        y: Int = 64,
        z: Int = 0,
        name: String? = "Test Waystone",
        waystoneUuid: UUID? = UUID.randomUUID(),
        primaryOwnerUuid: UUID? = UUID.randomUUID(),
        isLocked: Boolean = false,
    ): WaystoneInfo = WaystoneInfo(
        worldUid = world.uid,
        x = x,
        y = y,
        z = z,
        name = name,
        waystoneUuid = waystoneUuid,
        primaryOwnerUuid = primaryOwnerUuid,
        isLocked = isLocked,
    )

    fun locked(
        world: World,
        x: Int = 0,
        y: Int = 64,
        z: Int = 0,
        ownerUuid: UUID = UUID.randomUUID(),
    ): WaystoneInfo = waystone(
        world = world,
        x = x,
        y = y,
        z = z,
        name = "Locked Waystone",
        primaryOwnerUuid = ownerUuid,
        isLocked = true,
    )

    fun unowned(
        world: World,
        x: Int = 0,
        y: Int = 64,
        z: Int = 0,
    ): WaystoneInfo = waystone(
        world = world,
        x = x,
        y = y,
        z = z,
        name = "Unowned Waystone",
        waystoneUuid = null,
        primaryOwnerUuid = null,
    )

    fun location(
        world: World,
        x: Int = 0,
        y: Int = 64,
        z: Int = 0,
    ): Location = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
}
