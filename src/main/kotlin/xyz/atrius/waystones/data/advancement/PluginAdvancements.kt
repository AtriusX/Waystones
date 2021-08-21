package xyz.atrius.waystones.data.advancement

import org.bukkit.Material

val A_NEW_KIND_OF_TUNNEL = Advancement(
    Material.LODESTONE,
    "A New Kind of Tunnel",
    "Use a waystone for the first time."
)

val GIGAWARPS = Advancement(
    Material.REDSTONE_BLOCK,
    "1.21 Gigawarps",
    "Travel over 50% of the max warp distance through a waystone",
    A_NEW_KIND_OF_TUNNEL
)

val I_DONT_FEEL_SO_GOOD = Advancement(
    Material.WITHER_ROSE,
    "I don't feel so good...",
    "Get hit with portal sickness",
    A_NEW_KIND_OF_TUNNEL
)

val UNLIMITED_POWER = Advancement(
    Material.TNT,
    "UNLIMITED POWER",
    "Overcharge a waystone causing it to explode",
    A_NEW_KIND_OF_TUNNEL
)

val QUANTUM_DOMESTICATION = Advancement(
    Material.NAME_TAG,
    "Quantum Domestication",
    "Give a waystone a name",
    A_NEW_KIND_OF_TUNNEL
)

val BLOCKED = Advancement(
    Material.OBSIDIAN,
    "Blocked",
    "Suppress a waystone",
    A_NEW_KIND_OF_TUNNEL
)

val SHOOT_THE_MESSENGER = Advancement(
    Material.SKELETON_SKULL,
    "Shoot the Messenger",
    "Nearly die from a skeleton while attempting to warp away",
    A_NEW_KIND_OF_TUNNEL
)

val CLEAN_ENERGY = Advancement(
    Material.BEACON,
    "Clean Energy",
    "Use a waystone powered off the same energy source as a beacon",
    A_NEW_KIND_OF_TUNNEL
)

@Suppress("unused")
val HOW_LOW_CAN_YOU_GO = Advancement(
    Material.BLACK_CONCRETE_POWDER,
    "How Low Can You Go?",
    "Fall into Limbo while attempting to warp",
    A_NEW_KIND_OF_TUNNEL
)