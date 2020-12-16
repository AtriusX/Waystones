package xyz.atrius.waystones.data.config

import kotlin.reflect.KClass

interface ArgumentParser<T> {

    fun parse(input: String?): T?

    fun isValid(value: T): Boolean = true
}

sealed class IntParser : ArgumentParser<Int> {
    override fun parse(input: String?): Int? = input?.toIntOrNull()

    companion object : IntParser()
}

object PositiveValueParser : IntParser() {
    override fun isValid(value: Int): Boolean = value >= 0
}

object DoubleParser : ArgumentParser<Double> {
    override fun parse(input: String?): Double? = input?.toDoubleOrNull()
}

object PercentageParser : ArgumentParser<Double> {
    override fun parse(input: String?): Double? = if (input?.matches("[0-9]+%".toRegex()) == true)
        input.dropLast(1).toDouble() / 100 else null
}

object BooleanParser : ArgumentParser<Boolean> {
    override fun parse(input: String?): Boolean? =
        if (input?.toLowerCase() in arrayOf("true", "false")) input.toBoolean() else null
}

class EnumParser<E : Enum<E>>(private val enum: KClass<E>) : ArgumentParser<E> {
    override fun parse(input: String?): E? {
        input ?: return null
        val values = enum.java.enumConstants
        return values.firstOrNull { it.name == input }
    }
}

class ArrayParser<T>(private val parser: ArgumentParser<T>) : ArgumentParser<Array<T>> {

    @Suppress("UNCHECKED_CAST")
    override fun parse(input: String?): Array<T>? {
        val data = input?.split(" ") ?: return null
        val arr = mutableListOf<Any>()
        // Populate the array
        for (item in data)
            arr += (parser.parse(item) ?: return null)
        return arr as Array<T>?
    }
}