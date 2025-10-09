package xyz.atrius.waystones.command.waystones

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands.argument
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.koin.core.annotation.Single
import xyz.atrius.waystones.dao.WaystoneInfo
import xyz.atrius.waystones.manager.LocalizationManager
import xyz.atrius.waystones.repository.WaystoneInfoRepository
import xyz.atrius.waystones.utility.center
import xyz.atrius.waystones.utility.message
import xyz.atrius.waystones.utility.translateColors
import java.util.UUID
import kotlin.math.ceil

@Single
class ListCommand(
    private val waystoneInfoRepository: WaystoneInfoRepository,
    private val localization: LocalizationManager,
) : WaystoneSubcommand {

    companion object {
        private const val PAGE_SIZE = 10
    }

    override val name: String = "list"

    override val basePermission: String = "waystones.admin.list"

    private val teleportSound = Sound.sound(
        Key.key("entity.experience_orb.pickup"),
        Sound.Source.UI,
        1f,
        1f
    )

    override fun build(base: ArgumentBuilder<CommandSourceStack, *>): ArgumentBuilder<CommandSourceStack, *> = base
        .then(
            argument("page", IntegerArgumentType.integer(1, pageCount()))
                .executes {
                    val page = it.getArgument("page", Int::class.java)

                    listEntries(it.source.sender, page)
                }
        )
        .executes { listEntries(it.source.sender) }

    private fun listEntries(sender: CommandSender, page: Int = 1): Int {
        val offset = (page - 1) * PAGE_SIZE
        val waystones = waystoneInfoRepository
            .getAll(PAGE_SIZE, offset)
            .get()
        val unnamedWaystone = localization["unnamed-waystone"]
            .format(sender as? Player)

        if (waystones.isEmpty()) {
            emptyWaystoneList(sender, page)
            return Command.SINGLE_SUCCESS
        }

        sender.message(localization["plugin-header"])

        val worlds = mutableMapOf<UUID, World?>()
        val hover = localization["waystone-list-hover"]
            .format(sender as? Player)
            .let(Component::text)

        for (info in waystones) {
            val world = worlds
                .computeIfAbsent(info.worldUid) { Bukkit.getWorld(info.worldUid) }
                ?: continue
            val entry = getTeleportComponent(world.name, info, hover) {
                click(sender, world, info)
            }
                .appendSpace()
                .append(getNameComponent(info, unnamedWaystone))

            sender.sendMessage(entry)
        }

        sender.message(localization["plugin-footer"])
        return Command.SINGLE_SUCCESS
    }

    fun click(sender: CommandSender, world: World, info: WaystoneInfo) {
        if (sender !is Player) {
            return
        }

        val location = world
            .getBlockAt(info.x, info.y + 1, info.z)
            .location

        sender.teleport(location.center)
        sender.playSound(teleportSound)
    }

    private fun pageCount(): Int = waystoneInfoRepository
        .entries()
        .thenApplyAsync { ceil(it.toDouble() / PAGE_SIZE).toInt() }
        .get()
        .coerceAtLeast(1)

    private fun emptyWaystoneList(sender: CommandSender, page: Int) = when (page) {
        1 -> sender.message(localization["waystone-list-empty"])
        else -> sender.message(localization["waystone-list-no-entries", page])
    }

    private fun getTeleportComponent(
        name: String,
        info: WaystoneInfo,
        hover: Component,
        click: () -> Unit,
    ): Component = Component
        .text("&d&l[&7$name@${info.x}:${info.y}:${info.z}&d&l]".translateColors())
        .clickEvent(ClickEvent.callback { click() })
        .hoverEvent(HoverEvent.showText(hover))

    private fun getNameComponent(info: WaystoneInfo, default: String): Component = Component
        .text("&r&b&o${info.name ?: default}".translateColors())
}
