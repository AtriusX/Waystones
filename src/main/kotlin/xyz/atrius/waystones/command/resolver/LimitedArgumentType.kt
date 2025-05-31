package xyz.atrius.waystones.command.resolver

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.papermc.paper.command.brigadier.argument.CustomArgumentType
import xyz.atrius.waystones.utility.sanitizedStringFormat
import java.util.concurrent.CompletableFuture

open class LimitedArgumentType<T : Any>(
    values: Array<T>,
) : CustomArgumentType<T, String> {
    private val names = values
        .associateBy { it.sanitizedStringFormat() }

    override fun parse(reader: StringReader): T {
        val value = reader
            .readString()
            .sanitizedStringFormat()

        return names[value]
            ?: error("Failed to find value '$value'! Known values: ${names.keys.joinToString(", ")}")
    }

    override fun <S : Any> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        for (name in names.keys) {
            if (name.startsWith(builder.remainingLowerCase)) {
                builder.suggest(name)
            }
        }

        return builder.buildFuture()
    }

    override fun getNativeType(): StringArgumentType {
        return StringArgumentType.word()
    }
}
