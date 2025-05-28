package xyz.atrius.waystones.data.config

import xyz.atrius.waystones.utility.translateColors

class LocalizedString(
    private val key: String,
    private val localization: Localization,
    private vararg val args: Any?,
) {

    fun format(vararg args: Any?): String {
        return localization.getTemplate(key).format(args).translateColors()
    }

    override fun toString(): String {
        return format(*args)
    }
}
