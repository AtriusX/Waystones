package xyz.atrius.waystones.provider

import org.bukkit.Material
import org.bukkit.NamespacedKey
import xyz.atrius.waystones.data.config.LocalizedString
import xyz.atrius.waystones.data.json.advancement.Advancement
import xyz.atrius.waystones.data.json.advancement.AdvancementType
import xyz.atrius.waystones.data.json.advancement.Display
import xyz.atrius.waystones.data.json.advancement.Icon
import xyz.atrius.waystones.data.json.advancement.Text
import xyz.atrius.waystones.utility.toKey

open class AdvancementProvider(
    private val title: LocalizedString,
    private val description: LocalizedString,
    private val item: Material,
    private val parent: AdvancementProvider? = null,
    private val type: AdvancementType = AdvancementType.TASK,
) {

    val asAdvancement: Advancement by lazy {
        val display = Display(
            title = Text(title.toString()),
            description = Text(description.toString()),
            icon = Icon(item),
            frame = type,
        )

        Advancement(
            display = display,
            parent = parent
                ?.namespacedKey()
                ?.asString(),
        )
    }

    fun namespacedKey(): NamespacedKey = asAdvancement
        .key()
        .toKey()

    private fun Advancement.key(): String =
        display.title.text
            .lowercase()
            .replace(" ", "_")
            .replace("\\W".toRegex(), "")
}
