package xyz.atrius.waystones.data.advancement

import org.bukkit.Material
import xyz.atrius.waystones.data.Json

data class Advancement(
    val item: Material,
    val text: String,
    val description: String
) : Json<Advancement> {

    override fun toJsonString(): String = """
        |{
        |  "display": {
        |    "title": {
        |      "text": $text
        |    }
        |  },
        |  "description": {
        |    "text": $description
        |  },
        |  "icon": {
        |    "item": "minecraft:${item.toString().toLowerCase()}"
        |  }
        |  "show_toast": true,
        |  "announce_to_chat": true,
        |  "hidden": false
        |}
    """.trimMargin()
}
