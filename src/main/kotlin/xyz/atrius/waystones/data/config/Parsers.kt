package xyz.atrius.waystones.data.config

import java.util.*
import kotlin.reflect.KClass

interface ArgumentParser<T> {

    /**
     * Parses the given data into the specified output type
     * this will return null if there is a problem with parsing
     * the data.
     *
     * @param input The input value to parse.
     * @property T The parser return type. Null if error occurs.
     * @return The parsed data, or null if an error occurred.
     */
    fun parse(input: Any?): T?

    fun toString(value: T): String =
        value.toString()
}

sealed class IntParser : ArgumentParser<Int> {
    override fun parse(input: Any?): Int? = input?.toString()?.toIntOrNull()

    companion object : IntParser()
}

object PositiveValueParser : IntParser() {

    override fun parse(input: Any?): Int? {
        val num = super.parse(input) ?: return null
        return if (isValid(num)) num else null
    }

    private fun isValid(value: Int): Boolean = value >= 0
}

class RangeParser(private val range: IntRange) : IntParser() {

    override fun parse(input: Any?): Int? {
        val value = super.parse(input)
        return if (value in range) value else null
    }
}

object BooleanParser : ArgumentParser<Boolean> {
    override fun parse(input: Any?): Boolean? {
        val str = input?.toString()
        return when (str?.lowercase()) {
            "1", "t", "true"  -> true
            "0", "f", "false" -> false
            else              -> null
        }
    }
}

class EnumParser<E : Enum<E>>(private val enum: KClass<E>) : ArgumentParser<E> {
    override fun parse(input: Any?): E? {
        input ?: return null
        val values = enum.java.enumConstants
        return values.firstOrNull { it.name.equals(input.toString(), true) }
    }
}
