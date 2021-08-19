package xyz.atrius.waystones.data.advancement

import org.bukkit.Material
import org.intellij.lang.annotations.Language
import xyz.atrius.waystones.data.json.Json
import xyz.atrius.waystones.plugin
import xyz.atrius.waystones.utility.toKey
import org.bukkit.advancement.Advancement as SpigotAdvancement

data class Advancement(
    val item: Material,
    val title: String,
    val description: String,
    @Language("JSON") val nbt: String = "{}",
    val showToast: Boolean = true,
    val announceToChat: Boolean = true,
    val hidden: Boolean = false
) : Json<Advancement> {
    @Language("JSON")
    override fun toJson(): String = """
        |{
        |  "display": {
        |    "title": {
        |      "text": $title
        |    }
        |  },
        |  "description": {
        |    "text": $description
        |  },
        |  "icon": {
        |    "item": ${item.key},
        |    "nbt": "$nbt"
        |  },
        |  "criteria": {
        |    "command": {
        |      "trigger": "minecraft:impossible"
        |    }
        |  }
        |  "show_toast": $showToast,
        |  "announce_to_chat": $announceToChat,
        |  "hidden": $hidden
        |}
    """.trimMargin()

    fun toInstance(): SpigotAdvancement =
        plugin.server.getAdvancement(title.toKey())!!

    fun paired(): Pair<String, Advancement> =
        title.toLowerCase().replace(" ", "_") to this
}
