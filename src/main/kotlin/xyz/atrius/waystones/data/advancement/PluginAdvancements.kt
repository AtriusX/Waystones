package xyz.atrius.waystones.data.advancement

import org.bukkit.Material

val WAYSTONES = Advancement(
    Material.LODESTONE,
    "Waystones",
    "There and back again"
)

val SECRET_TUNNEL = Advancement(
    Material.COMPASS,
    "Secret Tunnel",
    "Use a waystone for the first time",
    WAYSTONES
)

val GIGAWARPS = Advancement(
    Material.REDSTONE_BLOCK,
    "1.21 Gigawarps",
    "Travel over 50% of the max warp distance through a waystone",
    SECRET_TUNNEL
)

val I_DONT_FEEL_SO_GOOD = Advancement(
    Material.WITHER_ROSE,
    "I don't feel so good...",
    "Get hit with portal sickness",
    SECRET_TUNNEL
)

val UNLIMITED_POWER = Advancement(
    Material.TNT,
    "UNLIMITED POWER",
    "Overcharge a waystone causing it to explode",
    SECRET_TUNNEL
)

val QUANTUM_DOMESTICATION = Advancement(
    Material.NAME_TAG,
    "Quantum Domestication",
    "Give a waystone a name",
    SECRET_TUNNEL
)

val BLOCKED = Advancement(
    Material.OBSIDIAN,
    "Blocked",
    "Suppress a waystone",
    SECRET_TUNNEL
)

val SHOOT_THE_MESSENGER = Advancement(
    Material.SKELETON_SKULL,
    "Shoot the Messenger",
    "Nearly die from a skeleton while attempting to warp away",
    SECRET_TUNNEL
)

val CLEAN_ENERGY = Advancement(
    Material.BEACON,
    "Clean Energy",
    "Use a waystone powered off the same energy source as a beacon",
    SECRET_TUNNEL
)

@Suppress("unused")
val HOW_LOW_CAN_YOU_GO = Advancement(
    Material.BLACK_CONCRETE_POWDER,
    "How Low Can You Go?",
    "Fall into Limbo while attempting to warp",
    SECRET_TUNNEL
)