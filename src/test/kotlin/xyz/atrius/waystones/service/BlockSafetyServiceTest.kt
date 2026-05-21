package xyz.atrius.waystones.service

import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.bukkit.Location
import org.bukkit.Material
import xyz.atrius.waystones.data.config.property.SafeLiquidsProperty
import xyz.atrius.waystones.data.config.property.type.SafeLiquids
import xyz.atrius.waystones.test.ServerFunSpec

class BlockSafetyServiceTest : ServerFunSpec({

    val checkLocation by lazy {
        Location(world, 0.0, 64.0, 0.0)
    }

    fun subject(policy: SafeLiquids): BlockSafetyService {
        val property = mockk<SafeLiquidsProperty>()
        every { property.value() } returns policy
        return BlockSafetyService(property)
    }

    fun clearBlocksAbove() {
        world.getBlockAt(0, 65, 0).type = Material.AIR
        world.getBlockAt(0, 66, 0).type = Material.AIR
    }

    fun placeBlockAtFeet(material: Material) {
        world.getBlockAt(0, 65, 0).type = material
    }

    fun placeBlockAtHead(material: Material) {
        world.getBlockAt(0, 66, 0).type = material
    }

    context("BlockSafetyService") {

        context("isLocationSafe") {

            test("Returns true when all blocks above are air") {
                clearBlocksAbove()
                val service = subject(SafeLiquids.ALL)

                service.isLocationSafe(checkLocation) shouldBe true
            }

            test("Returns false when world is null") {
                val nullLocation = Location(null, 0.0, 65.0, 0.0)
                val service = subject(SafeLiquids.ALL)

                service.isLocationSafe(nullLocation) shouldBe false
            }

            context("Physical obstructions") {

                test("Returns false when a collidable block is above at Y+1") {
                    clearBlocksAbove()
                    placeBlockAtFeet(Material.STONE)
                    val service = subject(SafeLiquids.ALL)

                    service.isLocationSafe(checkLocation) shouldBe false
                }

                test("Returns false when a collidable block is above at Y+2") {
                    clearBlocksAbove()
                    placeBlockAtHead(Material.GLASS)
                    val service = subject(SafeLiquids.ALL)

                    service.isLocationSafe(checkLocation) shouldBe false
                }
            }

            context("Signs should not obstruct") {
                withData(
                    Material.OAK_SIGN,
                    Material.SPRUCE_SIGN,
                    Material.BIRCH_SIGN,
                    Material.JUNGLE_SIGN,
                    Material.ACACIA_SIGN,
                    Material.DARK_OAK_SIGN,
                    Material.MANGROVE_SIGN,
                    Material.CHERRY_SIGN,
                    Material.BAMBOO_SIGN,
                    Material.CRIMSON_SIGN,
                    Material.WARPED_SIGN,
                    Material.OAK_WALL_SIGN,
                    Material.SPRUCE_WALL_SIGN,
                    Material.OAK_HANGING_SIGN,
                    Material.SPRUCE_HANGING_SIGN,
                    Material.OAK_WALL_HANGING_SIGN,
                ) { material ->
                    clearBlocksAbove()
                    placeBlockAtFeet(material)
                    val service = subject(SafeLiquids.ALL)

                    service.isLocationSafe(checkLocation) shouldBe true
                }
            }

            context("Doors should obstruct") {
                withData(
                    Material.OAK_DOOR,
                    Material.SPRUCE_DOOR,
                    Material.BIRCH_DOOR,
                    Material.JUNGLE_DOOR,
                    Material.ACACIA_DOOR,
                    Material.DARK_OAK_DOOR,
                    Material.MANGROVE_DOOR,
                    Material.CHERRY_DOOR,
                    Material.BAMBOO_DOOR,
                    Material.CRIMSON_DOOR,
                    Material.WARPED_DOOR,
                    Material.IRON_DOOR,
                ) { material ->
                    clearBlocksAbove()
                    placeBlockAtFeet(material)
                    val service = subject(SafeLiquids.ALL)

                    service.isLocationSafe(checkLocation) shouldBe false
                }
            }

            context("Potted plants should obstruct") {
                withData(
                    Material.FLOWER_POT,
                    Material.POTTED_OAK_SAPLING,
                    Material.POTTED_RED_MUSHROOM,
                    Material.POTTED_CACTUS,
                    Material.POTTED_BAMBOO,
                ) { material ->
                    clearBlocksAbove()
                    placeBlockAtFeet(material)
                    val service = subject(SafeLiquids.ALL)

                    service.isLocationSafe(checkLocation) shouldBe false
                }
            }

            context("Redstone components") {

                context("Physical redstone components should obstruct") {
                    withData(
                        Material.REPEATER,
                        Material.COMPARATOR,
                        Material.OBSERVER,
                        Material.TARGET,
                        Material.DAYLIGHT_DETECTOR,
                        Material.NOTE_BLOCK,
                        Material.SCULK_SENSOR,
                        Material.SCULK_CATALYST,
                        Material.SCULK_SHRIEKER,
                        Material.CALIBRATED_SCULK_SENSOR,
                        Material.REDSTONE_BLOCK,
                        Material.REDSTONE_LAMP,
                    ) { material ->
                        clearBlocksAbove()
                        placeBlockAtFeet(material)
                        val service = subject(SafeLiquids.ALL)

                        service.isLocationSafe(checkLocation) shouldBe false
                    }
                }

                context("Non-physical redstone components should not obstruct") {
                    withData(
                        Material.REDSTONE_WIRE,
                        Material.REDSTONE_TORCH,
                        Material.REDSTONE_WALL_TORCH,
                        Material.LEVER,
                        Material.STONE_PRESSURE_PLATE,
                        Material.OAK_PRESSURE_PLATE,
                        Material.STONE_BUTTON,
                        Material.OAK_BUTTON,
                        Material.TRIPWIRE,
                        Material.TRIPWIRE_HOOK,
                        Material.SCULK_VEIN,
                    ) { material ->
                        clearBlocksAbove()
                        placeBlockAtFeet(material)
                        val service = subject(SafeLiquids.ALL)

                        service.isLocationSafe(checkLocation) shouldBe true
                    }
                }
            }

            context("Torches should not obstruct") {
                withData(
                    Material.TORCH,
                    Material.SOUL_TORCH,
                    Material.WALL_TORCH,
                    Material.SOUL_WALL_TORCH,
                ) { material ->
                    clearBlocksAbove()
                    placeBlockAtFeet(material)
                    val service = subject(SafeLiquids.ALL)

                    service.isLocationSafe(checkLocation) shouldBe true
                }
            }

            context("Hazard blocks should obstruct") {
                withData(
                    Material.FIRE,
                    Material.SOUL_FIRE,
                    Material.CAMPFIRE,
                    Material.SOUL_CAMPFIRE,
                    Material.SWEET_BERRY_BUSH,
                    Material.WITHER_ROSE,
                ) { material ->
                    clearBlocksAbove()
                    placeBlockAtFeet(material)
                    val service = subject(SafeLiquids.ALL)

                    service.isLocationSafe(checkLocation) shouldBe false
                }
            }

            context("Liquid policy") {

                context("Blocks lava when policy is NONE") {

                    test("NONE blocks lava") {
                        clearBlocksAbove()
                        placeBlockAtFeet(Material.LAVA)
                        val service = subject(SafeLiquids.NONE)

                        service.isLocationSafe(checkLocation) shouldBe false
                    }

                    test("WATER blocks lava") {
                        clearBlocksAbove()
                        placeBlockAtFeet(Material.LAVA)
                        val service = subject(SafeLiquids.WATER)

                        service.isLocationSafe(checkLocation) shouldBe false
                    }

                    test("ALL allows lava") {
                        clearBlocksAbove()
                        placeBlockAtFeet(Material.LAVA)
                        val service = subject(SafeLiquids.ALL)

                        service.isLocationSafe(checkLocation) shouldBe true
                    }
                }

                test("Blocks water when policy is NONE") {
                    clearBlocksAbove()
                    placeBlockAtFeet(Material.WATER)
                    val service = subject(SafeLiquids.NONE)

                    service.isLocationSafe(checkLocation) shouldBe false
                }

                test("Allows water when policy is WATER") {
                    clearBlocksAbove()
                    placeBlockAtFeet(Material.WATER)
                    val service = subject(SafeLiquids.WATER)

                    service.isLocationSafe(checkLocation) shouldBe true
                }

                test("Allows water when policy is ALL") {
                    clearBlocksAbove()
                    placeBlockAtFeet(Material.WATER)
                    val service = subject(SafeLiquids.ALL)

                    service.isLocationSafe(checkLocation) shouldBe true
                }
            }
        }
    }
})
