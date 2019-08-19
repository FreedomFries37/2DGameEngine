package joshuaradin.editor.commands

import joshuaradin.annotation.CommandTemplate
import joshuaradin.annotation.ParameterTemplate
import joshuaradin.core.CommandInterpreter
import joshuaradin.core.DefaultConverters
import joshuaradin.core.IMultiParameterConverter
import joshuaradin.core.ParameterValue
import joshuaradin.gameengine2d.engine.core.scene.Scene
import joshuaradin.gameengine2d.engine.core.scene.SceneManager

@CommandTemplate(["scene"], 1, converter = SceneConverter::class)
class SceneCommand {

    @ParameterTemplate(["--name", "-n"], arity = 1, required = true, converter = DefaultConverters.StringConverter::class)
    val name: String? = null
}

class SceneConverter : IMultiParameterConverter<Scene> {

    override fun convert(strs: Set<ParameterValue<*>>): Scene {
        return when(strs.getDefault<String>()) {
            "create" -> {
                SceneManager.addScene(strs.get<String>("--name"))!!
            }
            "load" -> {
                SceneManager.getScene(strs.get<String>("--name"))!!
            }
            else -> {
                throw CommandInterpreter.NoOptionException("No scene command")
            }
        }
    }
}