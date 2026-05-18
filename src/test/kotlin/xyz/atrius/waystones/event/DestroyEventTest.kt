package xyz.atrius.waystones.event

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.ExplosionResult
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import xyz.atrius.waystones.repository.WaystoneInfoRepository
import xyz.atrius.waystones.test.ServerFunSpec
import java.util.concurrent.TimeUnit

class DestroyEventTest : ServerFunSpec({

    test("Breaking a waystone removes it from the database") {
        val world = createWorld("world")
        val player = createPlayer()
        val waystone = placeWaystone(world, 0, 64, 0)

        val nameTag = ItemStack(Material.NAME_TAG)
        nameTag.editMeta { it.displayName(Component.text("Test Waystone")) }
        player.inventory.setItem(0, nameTag)
        player.inventory.heldItemSlot = 0
        simulateRightClick(player, waystone, nameTag)

        val repository = get<WaystoneInfoRepository>()
        repository.getWaystone(waystone.location).get(5, TimeUnit.SECONDS) shouldNotBe null

        val breakEvent = BlockBreakEvent(waystone, player)
        server.pluginManager.callEvent(breakEvent)

        repository.getWaystone(waystone.location).get(5, TimeUnit.SECONDS) shouldBe null
    }

    test("Breaking a non-waystone block does not affect the database") {
        val world = createWorld("world")
        val player = createPlayer()
        val stone = placeBlock(world, 0, 64, 0, Material.STONE)

        val breakEvent = BlockBreakEvent(stone, player)
        server.pluginManager.callEvent(breakEvent)

        breakEvent.isCancelled shouldBe false
    }

    test("Breaking a lodestone without a database entry does not crash") {
        val world = createWorld("world")
        val player = createPlayer()
        val waystone = placeWaystone(world, 0, 64, 0)

        val breakEvent = BlockBreakEvent(waystone, player)
        server.pluginManager.callEvent(breakEvent)

        breakEvent.isCancelled shouldBe false
    }

    test("Destroying a waystone via block explosion removes it from the database") {
        val world = createWorld("world")
        val waystone = placeWaystone(world, 0, 64, 0)

        val nameTag = ItemStack(Material.NAME_TAG)
        nameTag.editMeta { it.displayName(Component.text("Explosive Waystone")) }
        val player = createPlayer()
        player.inventory.setItem(0, nameTag)
        player.inventory.heldItemSlot = 0
        simulateRightClick(player, waystone, nameTag)

        val repository = get<WaystoneInfoRepository>()
        repository.getWaystone(waystone.location).get(5, TimeUnit.SECONDS) shouldNotBe null

        val explodeEvent = BlockExplodeEvent(
            waystone,
            waystone.state,
            mutableListOf(waystone),
            4f,
            ExplosionResult.DESTROY,
        )
        server.pluginManager.callEvent(explodeEvent)

        repository.getWaystone(waystone.location).get(5, TimeUnit.SECONDS) shouldBe null
    }

    test("Destroying a waystone via entity explosion removes it from the database") {
        val world = createWorld("world")
        val waystone = placeWaystone(world, 0, 64, 0)

        val nameTag = ItemStack(Material.NAME_TAG)
        nameTag.editMeta { it.displayName(Component.text("TNT Waystone")) }
        val player = createPlayer()
        player.inventory.setItem(0, nameTag)
        player.inventory.heldItemSlot = 0
        simulateRightClick(player, waystone, nameTag)

        val repository = get<WaystoneInfoRepository>()
        repository.getWaystone(waystone.location).get(5, TimeUnit.SECONDS) shouldNotBe null

        val tnt = world.spawn(waystone.location, TNTPrimed::class.java)
        val explodeEvent = EntityExplodeEvent(
            tnt,
            waystone.location,
            mutableListOf(waystone),
            4f,
            ExplosionResult.DESTROY,
        )
        server.pluginManager.callEvent(explodeEvent)

        repository.getWaystone(waystone.location).get(5, TimeUnit.SECONDS) shouldBe null
    }
})
