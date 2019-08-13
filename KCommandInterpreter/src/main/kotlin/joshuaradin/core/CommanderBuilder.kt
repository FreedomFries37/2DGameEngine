package joshuaradin.core

/**
 * General format: [parameters] [command]
 */
class CommanderBuilder{

    private val args: MutableCollection<Parameter<*>> = mutableSetOf()
    private val commands: MutableMap<String, Command<*>> = mutableMapOf()

    fun <T> addCommand(cmd: Command<T>) {
        for (name in cmd.names) {
            commands[name] = cmd
        }

    }

    fun <T> addParameters(parameter: Parameter<T>) {
        args.add(parameter)
    }

    fun build() : CommandInterpreter {
        return CommandInterpreter(args, commands)
    }
}