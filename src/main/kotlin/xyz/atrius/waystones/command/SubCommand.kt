package xyz.atrius.waystones.command

import com.mojang.brigadier.builder.ArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack

interface SubCommand {

    val name: String

    val basePermission: String?
        get() = null

    fun build(base: ArgumentBuilder<CommandSourceStack, *>): ArgumentBuilder<CommandSourceStack, *>
}
