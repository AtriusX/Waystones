package xyz.atrius.waystones.test

import org.mockbukkit.mockbukkit.ServerMock

/**
 * Custom ServerMock that disables the AsyncCatcher by always reporting
 * that we're on the main thread. This is necessary because Kotest runs
 * tests in coroutines which may switch threads between suspensions.
 */
class TestServerMock : ServerMock() {
    override fun isPrimaryThread(): Boolean = true
}
