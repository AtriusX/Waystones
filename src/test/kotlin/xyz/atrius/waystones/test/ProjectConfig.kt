package xyz.atrius.waystones.test

import io.kotest.core.config.AbstractProjectConfig

/**
 * Project-wide Kotest configuration.
 * Ensures all tests run sequentially for MockBukkit compatibility.
 */
object ProjectConfig : AbstractProjectConfig() {
    override val concurrentSpecs = 1
    override val concurrentTests = 1
}
