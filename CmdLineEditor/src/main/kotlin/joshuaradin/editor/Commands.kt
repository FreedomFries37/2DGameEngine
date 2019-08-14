package joshuaradin.editor

import joshuaradin.annotation.CommandTemplate
import joshuaradin.annotation.ParameterTemplate
import joshuaradin.annotation.TemplateConverter
import joshuaradin.core.Command
import joshuaradin.core.IMultiParameterConverter
import joshuaradin.core.ParameterValue
import joshuaradin.gameengine2d.engine.core.basic.GameObject

object Commands {

    @CommandTemplate(["quit", "exit"])
    private class Quit
    val quit = Command<Boolean>(arrayOf("quit", "exit"), 0, " ", listOf(), object : IMultiParameterConverter<Boolean> {
        override fun convert(strs: Set<ParameterValue<*>>): Boolean = true
    })

    @CommandTemplate(["create"], arity = 1)
    private class Create {

        @ParameterTemplate(additionalNames = ["-n", "--name"], arity = 1)
        val name: String = "empty"


        @ParameterTemplate(additionalNames = ["-p", "--parent"], arity = 1)
        val parent: GameObject? = null
    }
    val create: Command<GameObject> = TemplateConverter.convertCommand<Create, GameObject>()
}