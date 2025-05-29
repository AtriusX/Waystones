package xyz.atrius.waystones.data.config

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.exceptions.CommandSyntaxException
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

open class ListConfigProperty<T : Any>(
    override val property: String,
    override val default: List<T>,
    override val parser: ArgumentType<T>,
    override val propertyType: KClass<out T>,
    val sizes: Set<Int>,
) : ConfigPropertyBase<T, List<T>, List<Any?>> {
    final override var value: List<T> = default
        private set

    override fun update(value: List<Any?>): Boolean {
        if (value.size !in sizes) {
            return false
        }

        try {
            this.value = value
                .map {
                    parser.parse(StringReader(it.toString()))
                }
        } catch (e: CommandSyntaxException) {
            logger.warn("Failed to parse update value for property '$property': ${e.message}")
            return false
        }

        return true
    }

    companion object {

        private val logger = LoggerFactory
            .getLogger(ListConfigProperty::class.java)
    }
}
