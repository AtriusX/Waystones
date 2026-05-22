package xyz.atrius.waystones.test

import io.kotest.core.extensions.SpecExtension
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.FunSpec
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.koin.core.context.stopKoin
import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock
import xyz.atrius.waystones.Waystones

/**
 * Base test class that manages a full MockBukkit server lifecycle.
 *
 * Each test spec gets:
 * - A fresh MockBukkit server instance
 * - The Waystones plugin loaded with full Koin DI
 * - A temporary SQLite database (auto-cleaned)
 * - Access to all plugin services via Koin
 *
 * Usage:
 * ```
 * class MyTest : ServerFunSpec({
 *     test("something works") {
 *         val player = createPlayer()
 *         val world = createWorld()
 *         val waystone = placeWaystone(world)
 *         // ... assertions
 *     }
 * })
 * ```
 */
abstract class ServerFunSpec private constructor() : FunSpec() {

    private var _server: ServerMock? = null
    private var _plugin: Waystones? = null
    private var _koin: org.koin.core.Koin? = null
    private var _world: World? = null

    internal val server: ServerMock get() = _server!!
    internal val plugin: Waystones get() = _plugin!!
    internal val koin: org.koin.core.Koin get() = _koin!!
    internal val world: World get() = _world!!

    constructor(body: ServerFunSpec.() -> Unit = {}) : this() {
        extensions(
            object : SpecExtension {
                override suspend fun intercept(spec: Spec, execute: suspend (Spec) -> Unit) {
                    _server = MockBukkit.mock()
                    _plugin = MockBukkit.load(Waystones::class.java)
                    _koin = _plugin!!.getKoinApp().koin
                    _world = _server!!.addSimpleWorld("test_world")

                    try {
                        execute(spec)
                    } finally {
                        MockBukkit.unmock()
                        stopKoin()
                    }
                }
            }
        )
        body()
    }

    /** Resolve a bean from the Koin container. Throws if not found. */
    internal inline fun <reified T : Any> get(): T = koin.get()

    /** Resolve a bean from the Koin container, or null if not found. */
    internal inline fun <reified T : Any> getOrNull(): T? = koin.getOrNull()

    /** Create a regular player. */
    internal fun createPlayer(name: String = "TestPlayer"): Player =
        server.addPlayer(name)

    /** Create an operator player. */
    internal fun createOpPlayer(name: String = "TestOp"): Player =
        server.addPlayer(name).also { it.isOp = true }

    /** Create a simple flat world. */
    internal fun createWorld(name: String = "test_world"): World =
        server.addSimpleWorld(name)

    /** Create a location in a world. */
    internal fun createLocation(
        world: World,
        x: Int = 0,
        y: Int = 64,
        z: Int = 0,
    ): Location = Location(world, x.toDouble(), y.toDouble(), z.toDouble())


    /** Place a block at the given coordinates. */
    internal fun placeBlock(
        world: World,
        x: Int = 0,
        y: Int = 64,
        z: Int = 0,
        material: Material,
    ): Block = world.getBlockAt(x, y, z).also { it.type = material }

    /** Place a waystone (lodestone) at the given coordinates. */
    internal fun placeWaystone(
        world: World,
        x: Int = 0,
        y: Int = 64,
        z: Int = 0,
    ): Block = placeBlock(world, x, y, z, Material.LODESTONE)

    /** Simulate a right-click on a block with an optional item in hand. */
    internal fun simulateRightClick(
        player: Player,
        block: Block,
        item: ItemStack? = null,
    ): PlayerInteractEvent = PlayerInteractEvent(
        player,
        Action.RIGHT_CLICK_BLOCK,
        item,
        block,
        org.bukkit.block.BlockFace.UP,
    ).also { server.pluginManager.callEvent(it) }

    /** Execute a command as the given sender. */
    internal fun executeCommand(
        sender: CommandSender,
        command: String,
    ): CommandResult {
        val commandMap = server.commandMap
        val parts = command.split(" ", limit = 2)
        val cmd = commandMap.getCommand(parts[0])
        val args = if (parts.size > 1) parts[1].split(" ").toTypedArray() else emptyArray()
        val success = cmd != null && cmd.execute(sender, parts[0], args)
        return CommandResult(success, "")
    }

    /** Execute a command as a player. */
    internal fun executePlayerCommand(
        player: Player,
        command: String,
    ): CommandResult = executeCommand(player, command)

    /** Execute a command from the server console. */
    internal fun executeConsoleCommand(
        command: String,
    ): CommandResult = executeCommand(server.consoleSender, command)
}

/** Result of a command execution. */
data class CommandResult(
    val success: Boolean,
    val output: String,
)
