package xyz.atrius.waystones.data.advancement

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.intellij.lang.annotations.Language
import xyz.atrius.waystones.data.json.Json
import xyz.atrius.waystones.localization
import xyz.atrius.waystones.plugin
import xyz.atrius.waystones.utility.toKey
import org.bukkit.advancement.Advancement as SpigotAdvancement

data class Text(
    val text: String
)

data class Icon(
    val item: String,
    @Language("JSON") val nbt: String? = null,
)

data class Trigger(
    val trigger: String
)

class Criteria(vararg items: Pair<String, String>) : Map<String, Trigger>
    by items.associate({ (name, trig) -> name to Trigger(trig) })

@Suppress("unused")
data class Display(
    val title: Text,
    val description: Text,
    val icon: Icon,
    val showToast: Boolean = true,
    val announceToChat: Boolean = true,
    val hidden: Boolean = false,
    val frame: AdvancementType? = null
) {
    val background = "minecraft:textures/block/chiseled_quartz_block.png"
}

data class Advancement(
    val display: Display,
    val criteria: Criteria,
    val parent: String? = null,
) : Json<Advancement> {

    constructor(
        item: Material,
        base: String,
        parent: Advancement? = null,
        frame: AdvancementType? = null
    ) : this(
        Display(
            Text(localization[base].toString()),
            Text(localization["$base-desc"].toString()),
            Icon(item.key.toString()),
            frame = frame
        ),
        IMPOSSIBLE,
        parent?.paired()?.first?.toString()
    )

    fun toInstance(): SpigotAdvancement? =
        plugin.server.getAdvancement(key())

    fun key(): NamespacedKey =
        display.title.text.toLowerCase().replace(" ", "_")
            .replace("[^\\w]".toRegex(), "").toKey()

    fun paired(): Pair<NamespacedKey, Advancement> =
        key() to this

    companion object {
        val IMPOSSIBLE = Criteria(
            "command" to "minecraft:impossible"
        )
    }
}
