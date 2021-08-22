package xyz.atrius.waystones.data.advancement

import org.bukkit.Material

val WAYSTONES = Advancement(
    Material.LODESTONE, "waystones"
)

val SECRET_TUNNEL = Advancement(
    Material.COMPASS, "secret-tunnel", WAYSTONES
)

val GIGAWARPS = Advancement(
    Material.REDSTONE_BLOCK, "gigawarps", SECRET_TUNNEL
)

val I_DONT_FEEL_SO_GOOD = Advancement(
    Material.WITHER_ROSE, "i-dont-feel-so-good", SECRET_TUNNEL
)

val UNLIMITED_POWER = Advancement(
    Material.TNT, "unlimited-power", SECRET_TUNNEL
)

val QUANTUM_DOMESTICATION = Advancement(
    Material.NAME_TAG, "quantum-domestication", SECRET_TUNNEL
)

val BLOCKED = Advancement(
    Material.OBSIDIAN, "blocked", SECRET_TUNNEL
)

val SHOOT_THE_MESSENGER = Advancement(
    Material.SKELETON_SKULL, "shoot-the-messenger", SECRET_TUNNEL
)

val CLEAN_ENERGY = Advancement(
    Material.BEACON, "clean-energy", SECRET_TUNNEL
)

@Suppress("unused")
val HOW_LOW_CAN_YOU_GO = Advancement(
    Material.BLACK_CONCRETE_POWDER, "how-low-can-you-go", SECRET_TUNNEL
)