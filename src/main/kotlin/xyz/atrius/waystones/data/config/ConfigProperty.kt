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
    override val format: (T) -> String = { it.toString() },
    override val readProcess: (T) -> T = { it },
    override val serialize: (T) -> Any = { it },
) : ConfigPropertyBase<T, T, Any?> {
    private var value: T = default

    override fun value(): T =
        readProcess(value)

    override fun format(): String =
        format(value)

    override fun serialize(): Any =
        serialize(value)

    override fun update(value: Any?): Boolean {
        try {
            this.value = parser
                .parse(StringReader(value.toString()))
        } catch (e: CommandSyntaxException) {
            logger.warn("Failed to parse update value for property '$property': ${e.message}")

            return false
        }

        return true
    }

    companion object {

        private val logger = LoggerFactory
            .getLogger(ConfigProperty::class.java)
    }
}
