package xyz.atrius.waystones.utility

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

const val WAYSTONES_NAMESPACE = "waystones"

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> getDataTypeFor(): PersistentDataType<T, T> {
    return when (T::class) {
        Int::class -> PersistentDataType.INTEGER as PersistentDataType<T, T>
        String::class -> PersistentDataType.STRING as PersistentDataType<T, T>
        Double::class -> PersistentDataType.DOUBLE as PersistentDataType<T, T>
        Float::class -> PersistentDataType.FLOAT as PersistentDataType<T, T>
        Long::class -> PersistentDataType.LONG as PersistentDataType<T, T>
        Byte::class -> PersistentDataType.BYTE as PersistentDataType<T, T>
        Boolean::class -> PersistentDataType.BOOLEAN as PersistentDataType<T, T>
        else -> throw IllegalArgumentException("Unsupported type: ${T::class.simpleName}")
    }
}

inline fun <reified T : Any> ItemStack.getPersistent(key: String): T? {
    val meta = this.itemMeta ?: return null
    val container = meta.persistentDataContainer
    val namespacedKey = NamespacedKey(WAYSTONES_NAMESPACE, key)
    return container.get(namespacedKey, getDataTypeFor<T>())
}

inline fun <reified T : Any> ItemStack.setPersistent(key: String, value: T) {
    val meta = this.itemMeta ?: return
    val container = meta.persistentDataContainer
    val namespacedKey = NamespacedKey(WAYSTONES_NAMESPACE, key)
    container.set(namespacedKey, getDataTypeFor<T>(), value)
    this.itemMeta = meta
}
