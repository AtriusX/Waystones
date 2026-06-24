package xyz.atrius.waystones.repository

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import xyz.atrius.waystones.test.ServerFunSpec
import java.util.UUID

class WaystoneAuthorizedUsersRepositoryTest : ServerFunSpec({

    val repository by lazy { get<WaystoneAuthorizedUsersRepository>() }

    test("add and check authorized user") {
        val waystoneId = UUID.randomUUID()
        val playerUuid = UUID.randomUUID()

        repository.addAuthorized(waystoneId, playerUuid).get().shouldBeTrue()
        repository.isAuthorized(waystoneId, playerUuid).get().shouldBeTrue()
    }

    test("isAuthorized returns false for non-authorized user") {
        val waystoneId = UUID.randomUUID()
        val playerUuid = UUID.randomUUID()

        repository.isAuthorized(waystoneId, playerUuid).get().shouldBeFalse()
    }

    test("remove authorized user") {
        val waystoneId = UUID.randomUUID()
        val playerUuid = UUID.randomUUID()

        repository.addAuthorized(waystoneId, playerUuid).get()
        repository.removeAuthorized(waystoneId, playerUuid).get().shouldBeTrue()
        repository.isAuthorized(waystoneId, playerUuid).get().shouldBeFalse()
    }

    test("getAuthorized returns all authorized users") {
        val waystoneId = UUID.randomUUID()
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()

        repository.addAuthorized(waystoneId, player1).get()
        repository.addAuthorized(waystoneId, player2).get()

        val authorized = repository.getAuthorized(waystoneId).get()
        assertSoftly {
            authorized shouldHaveSize 2
            authorized.map { it.ownerUuid } shouldContain player1
            authorized.map { it.ownerUuid } shouldContain player2
        }
    }

    test("getAuthorized returns empty list for waystone with no authorized users") {
        val waystoneId = UUID.randomUUID()

        repository.getAuthorized(waystoneId).get().shouldBeEmpty()
    }

    test("addAuthorized is idempotent") {
        val waystoneId = UUID.randomUUID()
        val playerUuid = UUID.randomUUID()

        repository.addAuthorized(waystoneId, playerUuid).get()
        repository.addAuthorized(waystoneId, playerUuid).get().shouldBeFalse()

        repository.isAuthorized(waystoneId, playerUuid).get().shouldBeTrue()
        repository.getAuthorized(waystoneId).get() shouldHaveSize 1
    }
})
