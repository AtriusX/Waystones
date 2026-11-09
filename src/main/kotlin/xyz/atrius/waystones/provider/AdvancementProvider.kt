package xyz.atrius.waystones.provider

import org.bukkit.Material
import org.bukkit.NamespacedKey
import xyz.atrius.waystones.data.json.advancement.Advancement
import xyz.atrius.waystones.data.json.advancement.AdvancementType
import xyz.atrius.waystones.data.json.advancement.Display
import xyz.atrius.waystones.data.json.advancement.Icon
import xyz.atrius.waystones.data.json.advancement.Text
import xyz.atrius.waystones.manager.LocalizedString
import xyz.atrius.waystones.utility.toKey

open class AdvancementProvider(
    private val title: LocalizedString,
    private val description: LocalizedString,
    private val item: Material,
    private val parent: AdvancementProvider? = null,
    private val type: AdvancementType = AdvancementType.TASK,
) {

    val asAdvancement: Advancement by lazy {
        val parent = parent
            ?.namespacedKey()
            ?.asString()
        val display = Display(
            title = Text(title.format(null)),
            description = Text(description.format(null)),
            icon = Icon(item),
            frame = type,
            // Parents can only exist on advancement roots now, avoid assigning
            // backgrounds to children to prevent loading issues at runtime.
            background = when (parent) {
                null -> "minecraft:block/lodestone_top"
                else -> null
            },
        )

        Advancement(
            display = display,
            parent = parent,
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
