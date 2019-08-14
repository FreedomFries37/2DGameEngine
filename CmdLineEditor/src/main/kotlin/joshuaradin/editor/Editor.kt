package joshuaradin.editor

import joshuaradin.core.CommandInterpreter
import joshuaradin.core.CommanderBuilder

object Editor {

    val interpreter: CommandInterpreter

    init {
        val builder = CommanderBuilder()
        builder.addCommand(Commands.create)
        builder.addCommand(Commands.quit)

        interpreter = builder.build()
    }
}