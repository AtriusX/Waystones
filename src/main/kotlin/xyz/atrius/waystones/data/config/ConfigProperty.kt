package xyz.atrius.waystones.data.config

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.exceptions.CommandSyntaxException
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

open class ConfigProperty<T : Any>(
    override val property: String,
    override val default: T,
    override val parser: ArgumentType<T>,
    override val propertyType: KClass<out T>,
    override val onUpdate: () -> Unit = {}
) : ConfigPropertyBase<T, T, Any?> {
    final override var value: T = default
        private set

    override fun update(value: Any?): Boolean {
        try {
            this.value = parser
                .parse(StringReader(value.toString()))
            onUpdate()
        } catch (e: CommandSyntaxException) {
            logger.error("Failed to parse updated value, ${e.message}")
            return false
        }

        return true
    }

    companion object {

        private val logger = LoggerFactory
            .getLogger(ConfigProperty::class.java)
    }
}
