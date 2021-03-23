package xyz.atrius.waystones.data.config

import java.util.Locale
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
    fun parse(input: String?): T?

    fun toString(value: T): String =
        value.toString()
}

object StringParser : ArgumentParser<String> {
    override fun parse(input: String?) = input
}

sealed class IntParser : ArgumentParser<Int> {
    override fun parse(input: String?): Int? = input?.toIntOrNull()

    companion object : IntParser()
}

object PositiveValueParser : IntParser() {

    override fun parse(input: String?): Int? {
        val num = super.parse(input) ?: return null
        return if (isValid(num)) num else null
    }

    private fun isValid(value: Int): Boolean = value >= 0
}

object DoubleParser : ArgumentParser<Double> {
    override fun parse(input: String?): Double? = input?.toDoubleOrNull()
}

object PercentageParser : ArgumentParser<Double> {
    private val regex = "^[0-9]+(.[0-9]+)?%$".toRegex()

    override fun parse(input: String?): Double? =
        if (input?.matches(regex) == true)
            input.dropLast(1).toDouble() / 100 else null

    override fun toString(value: Double): String =
        "${value * 100}%"
}

object BooleanParser : ArgumentParser<Boolean> {
    override fun parse(input: String?): Boolean? =
        if (input?.toLowerCase() in arrayOf("true", "false")) input.toBoolean() else null
}

object LocaleParser : ArgumentParser<Locale> {
    override fun parse(input: String?): Locale? = input?.let { Locale.forLanguageTag(input) }

    override fun toString(value: Locale): String = value.toLanguageTag()
}

class EnumParser<E : Enum<E>>(private val enum: KClass<E>) : ArgumentParser<E> {
    override fun parse(input: String?): E? {
        input ?: return null
        val values = enum.java.enumConstants
        return values.firstOrNull { it.name == input }
    }
}

class ListParser<T>(private val parser: ArgumentParser<T>) : ArgumentParser<List<T>> {
    @Suppress("UNCHECKED_CAST")
    override fun parse(input: String?): List<T>? {
        // Parse data in either list or vararg form
        val data = input?.removeSurrounding("[", "]")
            ?.split(" |, *".toRegex()) ?: return null
        val arr = mutableListOf<Any>()
        // Populate the array
        for (item in data)
            arr += (parser.parse(item) ?: return null)
        return arr as List<T>?
    }
}
