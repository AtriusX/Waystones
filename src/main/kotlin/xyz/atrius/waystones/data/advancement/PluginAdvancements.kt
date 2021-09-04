package xyz.atrius.waystones.data.advancement

import org.bukkit.Material
import xyz.atrius.waystones.data.advancement.AdvancementType.CHALLENGE
import xyz.atrius.waystones.data.advancement.AdvancementType.GOAL

val WAYSTONES = Advancement(
    Material.LODESTONE, "waystones"
)

val SECRET_TUNNEL = Advancement(
    Material.COMPASS, "secret-tunnel", WAYSTONES
)

val I_DONT_FEEL_SO_GOOD = Advancement(
    Material.WITHER_ROSE, "i-dont-feel-so-good", SECRET_TUNNEL
)

val HEAVY_ARTILLERY = Advancement(
    Material.NETHERITE_BLOCK, "heavy-artillery", SECRET_TUNNEL, GOAL
)

val CLEAN_ENERGY = Advancement(
    Material.BEACON, "clean-energy", HEAVY_ARTILLERY, GOAL
)

val GIGAWARPS = Advancement(
    Material.REDSTONE_BLOCK, "gigawarps", CLEAN_ENERGY, CHALLENGE
)

val UNLIMITED_POWER = Advancement(
    Material.GLOWSTONE, "unlimited-power", HEAVY_ARTILLERY, CHALLENGE
)

val QUANTUM_DOMESTICATION = Advancement(
    Material.NAME_TAG, "quantum-domestication", SECRET_TUNNEL
)

val BLOCKED = Advancement(
    Material.OBSIDIAN, "blocked", SECRET_TUNNEL
)

val SHOOT_THE_MESSENGER = Advancement(
    Material.SKELETON_SKULL, "shoot-the-messenger", UNLIMITED_POWER, CHALLENGE
)

@Suppress("unused")
val HOW_LOW_CAN_YOU_GO = Advancement(
    Material.BLACK_CONCRETE_POWDER, "how-low-can-you-go", SECRET_TUNNEL
)