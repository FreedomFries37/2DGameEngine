package joshuaradin.editor

import joshuaradin.annotation.TemplateConverter
import joshuaradin.core.CommandInterpreter
import joshuaradin.core.CommanderBuilder
import joshuaradin.editor.commands.SceneCommand
import joshuaradin.editor.commands.SystemCommands
import joshuaradin.editor.exceptions.NoProjectLoadedException
import joshuaradin.editor.exceptions.ProjectLoadedException
import joshuaradin.editor.exceptions.UnexpectedEditorBehavior
import joshuaradin.gameengine2d.engine.core.ProjectInfo
import joshuaradin.gameengine2d.engine.core.scene.Scene
import joshuaradin.gameengine2d.engine.core.service.GameObjectTracker
import java.util.*

object Editor {

    private val interpreter: CommandInterpreter
    private val sceneCommand = TemplateConverter.convertCommand<SceneCommand, Scene>()

    init {
        val builder = CommanderBuilder()
        builder.addCommand(SystemCommands.create)
        builder.addCommand(SystemCommands.quit)
        builder.addCommand(SystemCommands.save)
        builder.addCommand(SystemCommands.load)
        builder.addCommand(SystemCommands.dirty)
        builder.addCommand(SystemCommands.info)
        builder.addCommand(sceneCommand)

        interpreter = builder.build()
    }

    var info: ProjectInfo? = null
        set(value) {
            if (info != null) throw ProjectLoadedException()
            field = value
        }

    private val loaded: Boolean get() = info != null
    private var currentScene: Scene? = null
    private var continueExecution: Boolean = true

    private var dirty: Boolean = false



    fun run() {
        while (continueExecution) {
            print("${if(currentScene != null) "[$currentScene]" else ""}>> ")
            val input = readLine()
            if(input.isNullOrBlank()) continue

            runCommand(input)
            println()
        }
    }

    fun runCommand(cmd: String) {
        try {
            interpreter.parse(cmd)
            if(!loaded && interpreter.commandParsed() != SystemCommands.load) {
                throw NoProjectLoadedException()
            }



            when(interpreter.commandParsed()?.tag) {
                SystemCommands.systemTag -> runSystemCommand()
                SceneCommand.tag -> runSceneCommand()
                null -> {
                    throw UnexpectedEditorBehavior()
                }
            }
        }catch (e: Exception) {
            System.err.println(e)
        }
    }

    private fun runSystemCommand() {
        when (interpreter.commandParsed()) {
            SystemCommands.quit -> {
                if(dirty) waitConfirmation("Project unsaved, are you sure you want to quit?") { continueExecution = false }
                else continueExecution = false

            }
            SystemCommands.load -> {
                try {

                } catch (e : ProjectLoadedException) {
                    System.err.println(e)
                }
            }
            SystemCommands.save -> {
                dirty = false
            }
            SystemCommands.create -> {
                dirty = true
            }
            SystemCommands.dirty -> {
                println("Dirty = $dirty")
            }
            SystemCommands.info -> {
                when(interpreter.getCommandParameterValue<Int>("--level")) {

                    0 -> GameObjectTracker.instance.printAllObjectSceneAndPosition()
                    1 -> GameObjectTracker.instance.printLayeredToStringInfo()
                    2 -> GameObjectTracker.instance.printFullInfo()
                    else -> throw IndexOutOfBoundsException()
                }
            }
        }
    }

    private fun runSceneCommand() {
        currentScene = interpreter.getCommandValue(interpreter.commandParsedHard()!!)
    }

    private fun <T> waitConfirmation(msg: String = "Are you sure?", after: () -> T) {
        print("$msg [y/n]: ")
        val scanner = Scanner(System.`in`)
        val rec =scanner.next()[0].toLowerCase()
        when(rec) {
            'y' -> {
                after()
                return
            }
            'n' -> return
            else -> {
                System.err.println("not an option")
                waitConfirmation(msg, after)
            }
        }
    }
}

fun main(args: Array<String>) {

    if(args.isNotEmpty()) Editor.runCommand(args.joinToString(" "))
    else {
        val info = ProjectInfo("new project")
        Editor.info = info
    }
    Editor.run()
}