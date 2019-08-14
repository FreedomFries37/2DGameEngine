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

    fun <T> addParameters(parameter1: Parameter<T>, parameter2: Parameter<T>, vararg parameters: Parameter<T>) {
        args.add(parameter1)
        args.add(parameter2)
        args.addAll(parameters)
    }

    fun build() : CommandInterpreter {
        return CommandInterpreter(args, commands)
    }
}