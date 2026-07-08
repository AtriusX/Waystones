package xyz.atrius.waystones.event

import io.kotest.assertions.nondeterministic.eventually
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.persistence.PersistentDataType
import xyz.atrius.waystones.repository.WaystoneInfoRepository
import xyz.atrius.waystones.test.ServerFunSpec
import xyz.atrius.waystones.utility.toKey
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds

class LinkEventTest : ServerFunSpec({

    test("Linking a compass to a waystone creates a database entry") {
        val world = createWorld("world")
        val player = createPlayer()
        val waystone = placeWaystone(world, 100, 64, 200)

        val compass = ItemStack(Material.COMPASS)
        val meta = compass.itemMeta as CompassMeta
        meta.persistentDataContainer.set("is_warp_key".toKey(), PersistentDataType.INTEGER, 1)
        compass.itemMeta = meta
        player.inventory.setItem(0, compass)
        player.inventory.heldItemSlot = 0

        simulateRightClick(player, waystone, compass)

        val repository = get<WaystoneInfoRepository>()
        eventually(1.seconds) {
            repository.getWaystone(waystone.location).get(5, TimeUnit.SECONDS) shouldNotBe null
        }
    }

    test("Linking a compass to a non-waystone block does nothing") {
        val world = createWorld("world")
        val player = createPlayer()
        val stone = placeBlock(world, 0, 64, 0, Material.STONE)

        val compass = ItemStack(Material.COMPASS)
        player.inventory.setItem(0, compass)
        player.inventory.heldItemSlot = 0

        simulateRightClick(player, stone, compass)

        val repository = get<WaystoneInfoRepository>()
        eventually(1.seconds) {
            repository.getWaystone(stone.location).get(5, TimeUnit.SECONDS) shouldBe null
        }
    }

    test("Linking a non-compass item to a waystone does nothing") {
        val world = createWorld("world")
        val player = createPlayer()
        val waystone = placeWaystone(world, 0, 64, 0)

        val stick = ItemStack(Material.STICK)
        player.inventory.setItem(0, stick)
        player.inventory.heldItemSlot = 0

        simulateRightClick(player, waystone, stick)

        val repository = get<WaystoneInfoRepository>()
        eventually(1.seconds) {
            repository.getWaystone(waystone.location).get(5, TimeUnit.SECONDS) shouldBe null
        }
    }

    test("Linking a compass without warp key marker does nothing") {
        val world = createWorld("world")
        val player = createPlayer()
        val waystone = placeWaystone(world, 0, 64, 0)

        val compass = ItemStack(Material.COMPASS)
        player.inventory.setItem(0, compass)
        player.inventory.heldItemSlot = 0

        simulateRightClick(player, waystone, compass)

        val repository = get<WaystoneInfoRepository>()
        eventually(1.seconds) {
            repository.getWaystone(waystone.location).get(5, TimeUnit.SECONDS) shouldBe null
        }
    }
})
