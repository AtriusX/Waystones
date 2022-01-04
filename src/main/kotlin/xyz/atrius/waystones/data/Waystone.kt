package xyz.atrius.waystones.data

import xyz.atrius.waystones.data.json.Json

data class Waystone(
    val name: String?
) : Json<Waystone>