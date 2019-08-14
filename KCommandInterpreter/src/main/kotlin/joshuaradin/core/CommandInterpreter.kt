package joshuaradin.core

class CommandInterpreter internal constructor(private val args: MutableCollection<Parameter<*>>, private val commands: MutableMap<String, Command<*>> ) {

    private var paramMap: MutableMap<Parameter<*>, Any?>  = mutableMapOf()
    private val paramsNameMap: Map<String, Parameter<*>>

    private var cmdMap: MutableMap<Command<*>, Any?>  = mutableMapOf()
    private val cmdParamMap: MutableMap<Parameter<*>, Any?>  = mutableMapOf()

    private val isParsed: MutableMap<Parameter<*>, Boolean> = mutableMapOf()
    private val joined: Set<Parameter<*>> = args.union(commands.values)
    private val joinedNameMap: Map<String, Parameter<*>>

    private val required: Set<Parameter<*>>


    companion object {
        internal fun String.tokenize() : List<String> {
            val output = mutableListOf<String>()
            var current = ""
            val spaceRegex = Regex("\\s")
            var inQuote = false
            var quoteChar: Char? = null
            var escaped = false

            for(c in toCharArray()) {
                if(!escaped) {
                    if (c == '\\') {
                        escaped = true
                    } else if ((c == '\'' || c == '"') && (!inQuote || c == quoteChar)) {
                        inQuote = !inQuote
                        if (!inQuote && current.isNotEmpty()) {
                            output += current
                            current = ""
                            quoteChar = null
                        } else {
                            quoteChar = c
                        }
                    } else {
                        if (!inQuote && spaceRegex.matches("" + c)) {
                            if (current.isNotBlank()) {
                                output += current
                            }

                            current = ""
                        } else {
                            current += c
                        }
                    }
                }else{
                    current += c
                    escaped = false
                }
            }

            if(current.isNotBlank()){
                output += current
            }

            return output
        }
    }
    
    init {
        val required = mutableSetOf<Parameter<*>>()
        for (parameter in args) {
            paramMap[parameter] = null
            if(parameter.required) required.add(parameter)
        }
        this.required = required

        paramsNameMap = mapOf(*(args.map {
            val param = it
            it.names.map {
                Pair(it, param)
            }
        }.flatten().toTypedArray()))


        for (cmd in commands) {
            cmdMap[cmd.value] = null
        }

        for (parameter in joined) {
            isParsed[parameter] = false
        }
        val map = paramsNameMap.entries.union(commands.entries).map { Pair(it.key, it.value) }
        joinedNameMap = mutableMapOf<String, Parameter<*>>(*map.toTypedArray())

    }




    fun parse(string: String) {
        paramMap.replaceAll { _, _ -> null}
        cmdMap.replaceAll { _, _ -> null}
        isParsed.replaceAll {_,_ -> false}

        val tokens = string.tokenize()
        var currentParameter: Parameter<*>? = null
        var command: Command<*>? = null
        var checkingParameters = true
        var optionsLeft = 0
        val options = mutableListOf<String>()

        val paramValues = mutableSetOf<ParameterValue<*>>()
        val cmdParamValues = mutableSetOf<ParameterValue<*>>()

        for (i in 0 until tokens.size) {

            val current = tokens[i]

            if(checkingParameters && optionsLeft == 0 && isCommand(current)){
                checkingParameters = false
                command = getCommandForName(current)
                optionsLeft = command!!.arity
                currentParameter = null
            }else if(checkingParameters) {

                if(currentParameter == null) {
                    currentParameter = getParameterForName(current)
                    optionsLeft = currentParameter!!.arity
                }else{
                    if(isName(current)) throw IncorrectArityException(currentParameter, currentParameter.arity - optionsLeft)
                    options.add(current)
                    optionsLeft--

                    if(optionsLeft == 0) {
                        val parameterValue: ParameterValue<*> = currentParameter.toValue(options)
                        options.clear()
                        isParsed[currentParameter] = true
                        paramValues.add(parameterValue)
                        currentParameter = null
                    }
                }


            } else {

                if(currentParameter == null && optionsLeft > 0) {
                    currentParameter = command!!.getParameterForNames(Command.defaultName)
                }

                if(currentParameter == null) {
                    currentParameter = command!!.getParameterForName(current)
                    optionsLeft = currentParameter!!.arity
                }else{
                    if(command!!.isName(current)) throw IncorrectArityException(currentParameter, currentParameter.arity - optionsLeft)
                    options.add(current)
                    optionsLeft--

                    if(optionsLeft == 0) {
                        val parameterValue: ParameterValue<*> = currentParameter.toValue(options)
                        options.clear()

                        isParsed[currentParameter] = true
                        cmdParamValues.add(parameterValue)
                        currentParameter = null
                    }
                }
            }

        }

        if(command != null) {
            isParsed[command] = true

            for (parameter in command.parameters) {
                if(parameter.required && isParsed[parameter] == false) {
                    throw RequiredOptionMissingException(parameter)
                }else if(isParsed[parameter] == false) {
                    cmdParamValues.add(parameter.default())
                }
            }
        }

        if(required.any { isParsed[it] == false }) {
            throw RequiredOptionsMissingException(required.filter { isParsed[it] == false })
        }

        for(missedOptions in paramMap.keys.filter { isParsed[it] == false }) {
            paramValues.add(missedOptions.default())
        }

        for (paramValue in paramValues) {
            paramMap[paramValue.parameter] = paramValue.value
        }

        if(command != null) {
            for (cmdParamValue in cmdParamValues) {
                cmdParamMap[cmdParamValue.parameter] = cmdParamValue.value
            }

            cmdMap[command] = command.cmdConverter(cmdParamValues)
        }

    }

    private fun getParameterForName(name: String) : Parameter<*>?{
        if(!paramsNameMap.containsKey(name)) throw NoOptionException(name)
        return paramsNameMap[name]
    }

    private fun getParameterForName(cmd: Command<*>, name: String) : Parameter<*>?{
        return cmd.getParameterForName(name)
    }

    private fun getCommandForName(name: String) : Command<*>? {
        return commands[name]
    }

    fun isCommand(name: String) : Boolean {
        return commands.containsKey(name)
    }

    fun isName(name: String) : Boolean {
        return joinedNameMap.containsKey(name)
    }
    fun Command<*>.isName(name: String) : Boolean {
        return try {
            this.getParameterForName(name) != null
        }catch (e: NoOptionException) {
            false
        }
    }

    fun <T> getParameterValue(parameter: String) : T? {
        val param = getParameterForName(parameter)
        param?: return null
        return paramMap[param] as? T?
    }
    
    fun <T> getCommandValue(command: String) : T {
        if(!isCommand(command)) throw NoOptionException(command)
        val cmd = getCommandForName(command)!!
        if(!parsed(command)) throw NotParsedException(cmd)
        try {
            return cmdMap[cmd]!! as T
        }catch (e: ClassCastException) {
            throw NoValueException(cmd)
        }
    }

    /**
     * Checks if either the parameter or command was parsed
     */
    fun parsed(command: String) : Boolean {
        return isParsed[joinedNameMap[command]] ?: false
    }

    fun commandParsed() : Command<*>? {
        for (command in commands.values) {
            if(isParsed[command] == true) return command
        }
        return null
    }

    class NotParsedException(notparsed: Parameter<*>) : Exception("$notparsed was not parsed!")
    class NoOptionException(notparsed: String) : Exception("$notparsed is not an available option!")
    class RequiredOptionMissingException(notparsed: Parameter<*>) : Exception("$notparsed is required but was not parsed!")
    class RequiredOptionsMissingException(notparsed: Collection<Parameter<*>>) : Exception("$notparsed is required but was not parsed!")
    class NoValueException(notparsed: Parameter<*>) : Exception("$notparsed has no value!")
    class IncorrectArityException(notparsed: Parameter<*>, arityFound: Int) : Exception("$notparsed needs ${notparsed.arity}, found $arityFound")
}