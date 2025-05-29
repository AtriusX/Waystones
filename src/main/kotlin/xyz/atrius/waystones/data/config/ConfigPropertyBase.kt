package xyz.atrius.waystones.data.config

import com.mojang.brigadier.arguments.ArgumentType
import kotlin.reflect.KClass

sealed interface ConfigPropertyBase<T : Any, D : Any, U> {

    val property: String

    val default: D

    val parser: ArgumentType<T>

    val propertyType: KClass<out T>

    val format: (D) -> String

    val readProcess: (D) -> D

    val javaClass: Class<out T>
        get() = propertyType.java

    fun value(): D

    fun update(value: U): Boolean

    fun format(): String

    fun getLocalizedInfoKey(): String {
        return "property-$property-info"
    }
}
