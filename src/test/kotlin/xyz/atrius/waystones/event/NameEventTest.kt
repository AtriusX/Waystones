package xyz.atrius.waystones.event

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import xyz.atrius.waystones.repository.WaystoneInfoRepository
import xyz.atrius.waystones.test.ServerFunSpec
import java.util.concurrent.TimeUnit

class NameEventTest : ServerFunSpec({

    test("Naming a waystone with a name tag persists to the database") {
        val world = createWorld("world")
        val player = createPlayer()
        val waystone = placeWaystone(world, 0, 64, 0)

        val nameTag = ItemStack(Material.NAME_TAG)
        nameTag.editMeta { meta: ItemMeta ->
            meta.displayName(Component.text("My Waystone"))
        }
        player.inventory.setItem(0, nameTag)
        player.inventory.heldItemSlot = 0

        simulateRightClick(player, waystone, nameTag)

        val repository = get<WaystoneInfoRepository>()
        val info = repository.getWaystone(waystone.location).get(5, TimeUnit.SECONDS)
        info?.name shouldBe "My Waystone"
    }

    test("Renaming a waystone updates the database entry") {
        val world = createWorld("world")
        val player = createPlayer()
        val waystone = placeWaystone(world, 0, 64, 0)

        val nameTag1 = ItemStack(Material.NAME_TAG)
        nameTag1.editMeta { it.displayName(Component.text("First Name")) }
        player.inventory.setItem(0, nameTag1)
        player.inventory.heldItemSlot = 0
        simulateRightClick(player, waystone, nameTag1)

        val nameTag2 = ItemStack(Material.NAME_TAG)
        nameTag2.editMeta { it.displayName(Component.text("Second Name")) }
        player.inventory.setItem(0, nameTag2)
        player.inventory.heldItemSlot = 0
        simulateRightClick(player, waystone, nameTag2)

        val repository = get<WaystoneInfoRepository>()
        val info = repository.getWaystone(waystone.location).get(5, TimeUnit.SECONDS)
        info?.name shouldBe "Second Name"
    }

    test("Using a non-name-tag item does not name the waystone") {
        val world = createWorld("world")
        val player = createPlayer()
        val waystone = placeWaystone(world, 0, 64, 0)

        val stick = ItemStack(Material.STICK)
        player.inventory.setItem(0, stick)
        player.inventory.heldItemSlot = 0
        simulateRightClick(player, waystone, stick)

        val repository = get<WaystoneInfoRepository>()
        val info = repository.getWaystone(waystone.location).get(5, TimeUnit.SECONDS)
        info?.name shouldBe null
    }

    test("Naming a waystone with a name tag without display name does nothing") {
        val world = createWorld("world")
        val player = createPlayer()
        val waystone = placeWaystone(world, 0, 64, 0)

        val nameTag = ItemStack(Material.NAME_TAG)
        player.inventory.setItem(0, nameTag)
        player.inventory.heldItemSlot = 0
        simulateRightClick(player, waystone, nameTag)

        val repository = get<WaystoneInfoRepository>()
        val info = repository.getWaystone(waystone.location).get(5, TimeUnit.SECONDS)
        info?.name shouldBe null
    }

    test("Naming a non-lodestone block does nothing") {
        val world = createWorld("world")
        val player = createPlayer()
        val stone = placeBlock(world, 0, 64, 0, Material.STONE)

        val nameTag = ItemStack(Material.NAME_TAG)
        nameTag.editMeta { it.displayName(Component.text("My Stone")) }
        player.inventory.setItem(0, nameTag)
        player.inventory.heldItemSlot = 0
        simulateRightClick(player, stone, nameTag)

        val repository = get<WaystoneInfoRepository>()
        val info = repository.getWaystone(stone.location).get(5, TimeUnit.SECONDS)
        info?.name shouldBe null
    }

    test("Naming a waystone with the same name is ignored") {
        val world = createWorld("world")
        val player = createPlayer()
        val waystone = placeWaystone(world, 0, 64, 0)

        val nameTag1 = ItemStack(Material.NAME_TAG)
        nameTag1.editMeta { it.displayName(Component.text("Same Name")) }
        player.inventory.setItem(0, nameTag1)
        player.inventory.heldItemSlot = 0
        simulateRightClick(player, waystone, nameTag1)

        val repository = get<WaystoneInfoRepository>()
        repository.getWaystone(waystone.location).get(5, TimeUnit.SECONDS)?.name shouldBe "Same Name"

        val nameTag2 = ItemStack(Material.NAME_TAG)
        nameTag2.editMeta { it.displayName(Component.text("Same Name")) }
        player.inventory.setItem(0, nameTag2)
        player.inventory.heldItemSlot = 0
        simulateRightClick(player, waystone, nameTag2)

        repository.getWaystone(waystone.location).get(5, TimeUnit.SECONDS)?.name shouldBe "Same Name"
    }
})
