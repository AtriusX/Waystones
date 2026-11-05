package xyz.atrius.waystones.command.resolver

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import io.papermc.paper.command.brigadier.argument.CustomArgumentType
import xyz.atrius.waystones.data.config.property.Xp

object LevelArgumentType : CustomArgumentType<Xp, String> {

    override fun parse(reader: StringReader): Xp {
        val input = reader.readString()
        val isLevels = input
            .endsWith("L", true)
        // Remove the 'L' suffix if present, parse the value as an integer
        val count = when (isLevels) {
            true -> input
                .dropLast(1)
                .toIntOrNull()
            else -> input
                .toIntOrNull()
        }
        // If the xp count is null or negative, throw an error
        if (count == null || count < 0) {
            error("Invalid XP value: '$input'")
        }
        // Return the parsed Xp object
        return Xp(count, isLevels)
    }

    override fun getNativeType(): ArgumentType<String> {
        return StringArgumentType.word()
    }
}