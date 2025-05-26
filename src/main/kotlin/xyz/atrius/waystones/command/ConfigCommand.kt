@file:Suppress("UnstableApiUsage")

package xyz.atrius.waystones.command

import io.papermc.paper.command.brigadier.argument.resolvers.selector.SelectorArgumentResolver
import xyz.atrius.waystones.Power

//interface PaperCommand {
//
//    fun execute(sender: CommandSender, args: Array<String>, flags: Flags)
//}

//@Single
//class TestCommand(override val subCommands: List<SubCommand<TestCommand>>) : BaseCommand<TestCommand>("find")
//
//@Single
//class FindPlayer : SubCommand<TestCommand>("target") {
//
//    override fun build(base: ArgumentBuilder<CommandSourceStack, *>): ArgumentBuilder<CommandSourceStack, *> = base
//        .then(
//            argument("target", ArgumentTypes.player()).executes {
//                val arg = it.getArgument("target", PlayerSelectorArgumentResolver::class.java)
//                val target = arg.resolve(it.source).firstOrNull()
//
//                it.source.sender.sendPlainMessage("That's ${target?.name ?: "Unknown!"}!")
//
//                Command.SINGLE_SUCCESS
//            }
//        )
//}
//
//@Single
//class FindEntity : SubCommand<TestCommand>("entity") {
//
//    override fun build(base: ArgumentBuilder<CommandSourceStack, *>): ArgumentBuilder<CommandSourceStack, *> = base
//        .then(
//            argument("entity", ArgumentTypes.entities()).executes {
//                val arg = it.getArgument("entity", EntitySelectorArgumentResolver::class.java)
//                val target = arg.resolve(it.source)
//
//                for (t in target) {
//                    it.source.sender.sendPlainMessage("That's ${t.name}!")
//                }
//
//                Command.SINGLE_SUCCESS
//            }
//        )
//}
//
//@Single
//class SubTest : SubCommand<TestCommand>("test") {
//
//    override fun build(base: ArgumentBuilder<CommandSourceStack, *>): ArgumentBuilder<CommandSourceStack, *> = base
//        .requires { it.sender.hasPermission("ws.test") }
//        .then(
//            argument("a", StringArgumentType.word())
//                .then(
//                    argument("b", StringArgumentType.word())
//                        .executes {
//                            val a = it.getArgument("a", String::class.java)
//                            val b = it.getArgument("b", String::class.java)
//
//                            it.source.sender.sendPlainMessage("$a: $b")
//                            Command.SINGLE_SUCCESS
//                        }
//                )
//        )
//}
//
//@Single
//class SelectEnum : SubCommand<TestCommand>("enum") {
//
//    override fun build(base: ArgumentBuilder<CommandSourceStack, *>): ArgumentBuilder<CommandSourceStack, *> = base
//        .then(
//            argument("enum", EnumType(Power::class)).executes {
//                val target = it.getArgument("enum", Power::class.java)
//                it.source.sender.sendPlainMessage("Power of $target!")
//                Command.SINGLE_SUCCESS
//            }
//        )
//}

interface PowerArgumentResolver : SelectorArgumentResolver<Power> {

}