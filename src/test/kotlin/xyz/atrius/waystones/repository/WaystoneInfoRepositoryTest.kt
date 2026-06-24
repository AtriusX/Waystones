package xyz.atrius.waystones.repository

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import xyz.atrius.waystones.test.ServerFunSpec
import xyz.atrius.waystones.test.fixtures.WaystoneInfoFixtures.location
import xyz.atrius.waystones.test.fixtures.WaystoneInfoFixtures.locked
import xyz.atrius.waystones.test.fixtures.WaystoneInfoFixtures.unowned
import xyz.atrius.waystones.test.fixtures.WaystoneInfoFixtures.waystone
import java.util.UUID

class WaystoneInfoRepositoryTest : ServerFunSpec({

    val repository by lazy { get<WaystoneInfoRepository>() }

    test("save and retrieve waystone by location") {
        val loc = location(world, x = 100, y = 64, z = 200)
        val expected = waystone(world, x = 100, y = 64, z = 200, name = "Test Waystone")

        repository.save(expected).get() shouldBe 1

        repository.getWaystone(loc).get().shouldNotBeNull {
            assertSoftly {
                name shouldBe "Test Waystone"
                waystoneUuid shouldBe expected.waystoneUuid
                primaryOwnerUuid shouldBe expected.primaryOwnerUuid
                isLocked.shouldBeFalse()
            }
        }
    }

    test("save waystone with null waystoneUuid keeps null") {
        val loc = location(world, x = 101, y = 64, z = 200)
        val expected = unowned(world, x = 101, y = 64, z = 200)

        repository.save(expected).get() shouldBe 1

        repository.getWaystone(loc).get().shouldNotBeNull {
            waystoneUuid.shouldBeNull()
            name shouldBe "Unowned Waystone"
        }
    }

    test("existsByLocation returns true for existing waystone") {
        val loc = location(world, x = 102, y = 64, z = 200)
        repository.save(waystone(world, x = 102, y = 64, z = 200)).get()

        repository.existsByLocation(loc).get().shouldBeTrue()
    }

    test("existsByLocation returns false for non-existing waystone") {
        val loc = location(world, x = 999, y = 64, z = 999)

        repository.existsByLocation(loc).get().shouldBeFalse()
    }

    test("getLockedCount returns correct count") {
        repository.save(locked(world, x = 104, y = 64, z = 200)).get()
        repository.save(
            waystone(world, x = 105, y = 64, z = 200, name = "Unlocked", isLocked = false)
        ).get()

        repository.getLockedCount().get() shouldBeGreaterThanOrEqual 1
    }

    test("save overwrites existing waystone at same location") {
        val loc = location(world, x = 106, y = 64, z = 200)
        val first = waystone(world, x = 106, y = 64, z = 200, name = "First")
        val second = waystone(world, x = 106, y = 64, z = 200, name = "Second")

        repository.save(first).get() shouldBe 1
        repository.save(second).get() shouldBe 1

        repository.getWaystone(loc).get().shouldNotBeNull {
            name shouldBe "Second"
        }
    }
})
