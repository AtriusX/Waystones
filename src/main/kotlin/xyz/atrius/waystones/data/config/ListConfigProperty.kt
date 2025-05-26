package xyz.atrius.waystones.data.config

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.exceptions.CommandSyntaxException
import kotlin.reflect.KClass

open class ListConfigProperty<T : Any>(
    override val property: String,
    override val default: List<T>,
    override val parser: ArgumentType<T>,
    override val propertyType: KClass<out T>,
    override val onUpdate: () -> Unit = {},
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

            onUpdate()
        } catch (e: CommandSyntaxException) {
            e.printStackTrace()
            return false
        }

        return true
    }
}