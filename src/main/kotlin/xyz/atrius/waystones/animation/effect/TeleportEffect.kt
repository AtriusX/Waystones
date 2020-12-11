package xyz.atrius.waystones.animation.effect

import org.bukkit.Location

interface TeleportEffect {

    fun start()

    fun animation(timer: Long)

    fun end()

    fun endAtLocation(location: Location)

    fun cancel()
}