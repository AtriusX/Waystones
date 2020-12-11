package xyz.atrius.waystones.handler

import org.bukkit.entity.Player

interface PlayerHandler {
    val player: Player

    val error: String?

    fun handle(): Boolean
}