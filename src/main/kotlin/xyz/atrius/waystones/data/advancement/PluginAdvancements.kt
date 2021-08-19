package xyz.atrius.waystones.data.advancement

import org.bukkit.Material

val SUBSPACE_TUNNEL = Advancement(
    Material.RESPAWN_ANCHOR,
    "Subspace Tunnel",
    "Use a waystone for the first time.",
    """
        |{
        |  "charges": 4
        |}
    """.trimMargin()
).paired()