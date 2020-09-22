package xyz.atrius.waystones.utility

inline fun <reified T : Enum<T>> valueOfOrDefault(plugin: KotlinPlugin, value: String?, default: T) = try {
    if (value != null) enumValueOf(value) else default
} catch (e: Exception) {
    plugin.logger.warning("Error occurred reading value $value, setting to default. Error: ${e.message}")
    default
}