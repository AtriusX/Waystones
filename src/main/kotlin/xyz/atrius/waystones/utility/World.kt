package xyz.atrius.waystones.utility

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.World

fun World.forceParticle(
    particle: Particle,
    location: Location,
    count: Int,
    offsetX: Double = 0.0,
    offsetY: Double = 0.0,
    offsetZ: Double = 0.0,
    extra: Double = 0.0,
) =
    spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, null, true)