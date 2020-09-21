package xyz.atrius.waystones.utility

inline fun <reified T : Enum<T>> valueOfOrDefault(value: String?, default: T) = try {
    if (value != null) enumValueOf(value) else default
} catch (ignored: Exception) {
    ignored.printStackTrace()
    default
}