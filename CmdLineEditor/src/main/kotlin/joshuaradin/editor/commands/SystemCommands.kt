package joshuaradin.editor.commands

import joshuaradin.annotation.CommandTemplate
import joshuaradin.annotation.ParameterTemplate
import joshuaradin.annotation.TemplateConverter
import joshuaradin.core.Command
import joshuaradin.core.DefaultConverters
import joshuaradin.core.IMultiParameterConverter
import joshuaradin.core.ParameterValue
import joshuaradin.gameengine2d.engine.core.ProjectInfo
import joshuaradin.gameengine2d.engine.core.basic.GameObject

object SystemCommands {
    const val systemTag = "system"

    val quit = Command<Boolean>(arrayOf("quit", "exit"), 0, " ", listOf(), object : IMultiParameterConverter<Boolean> {
        override fun convert(strs: Set<ParameterValue<*>>): Boolean = true
    }).addTag(systemTag)

    val dirty = Command<Boolean>(arrayOf("dirty"), 0, " ", listOf(), object : IMultiParameterConverter<Boolean> {
        override fun convert(strs: Set<ParameterValue<*>>): Boolean = true
    }).addTag(systemTag)

    @CommandTemplate(["create"], arity = 1, tag = systemTag)
    class Create {

        @ParameterTemplate(additionalNames = ["-n", "--name"], arity = 1)
        val name: String = "empty"


        @ParameterTemplate(additionalNames = ["-p", "--parent"], arity = 1)
        val parent: GameObject? = null
    }
    val create: Command<GameObject> = TemplateConverter.convertCommand<Create, GameObject>()

    @CommandTemplate(["load"], 1, tag = systemTag)
    class Load
    val load: Command<ProjectInfo?> = TemplateConverter.convertCommand<Load, ProjectInfo?>()

    @CommandTemplate(["save"], 0, tag = systemTag)
    class Save
    val save: Command<Boolean> = TemplateConverter.convertCommand<Save, Boolean>()

    @CommandTemplate(names = ["info"], tag = systemTag)
    class Info {

        @ParameterTemplate(additionalNames = ["-l", "--level"], arity = 1, converter = DefaultConverters.IntConverter::class)
        val level: Int = 0

    }
    val info: Command<Unit> = TemplateConverter.convertCommand<Info, Unit>()
}