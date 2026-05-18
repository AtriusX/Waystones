package xyz.atrius.waystones.test

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import xyz.atrius.waystones.manager.LocalizationManager

class ServerFunSpecTest : ServerFunSpec({

    test("Server and plugin are initialized") {
        plugin.isEnabled shouldBe true
    }

    test("Koin container is accessible") {
        get<LocalizationManager>() shouldNotBe null
    }
})
