package joshuaradin.core

class CommandInterpreter internal constructor(private val args: MutableCollection<Parameter<*>>, private val commands: MutableMap<String, Command<*>> ) {

    private val paramMap: MutableMap<Parameter<*>, Any?>  = mutableMapOf()
    private val paramsNameMap: Map<String, Parameter<*>>

    private val cmdMap: MutableMap<Command<*>, Any?>  = mutableMapOf()

    private val isParsed: MutableMap<Parameter<*>, Boolean> = mutableMapOf()
    private val joined: Set<Parameter<*>> = args.union(commands.values)
    private val joinedNameMap: Map<String, Parameter<*>>



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
                    } else if (c == '\'' || c == '"' && (!inQuote || c == quoteChar)) {
                        inQuote = !inQuote
                        if (!inQuote && current.isNotEmpty()) {
                            output += current
                            current = ""
                            quoteChar = null
                        } else {
                            quoteChar = c
                        }
                    } else {
                        if (spaceRegex.matches("" + c)) {
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

            return output
        }
    }
    
    init {
        for (parameter in args) {
            paramMap[parameter] = null
        }
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
        joinedNameMap = mutableMapOf<String, Parameter<*>>(*paramsNameMap.entries.map { Pair(it.key, it.value) }.toTypedArray())

    }




    fun parse(string: String) {
        val tokens = string.tokenize()
        var currentParameter: Parameter<*>? = null
        var command: Command<*>? = null
        var checkingParameters = true
        var arity: Int
        val options = mutableListOf<String>()

        for (i in 0 until tokens.size) {

            if(checkingParameters) {
                
            }

        }
    }
    
    private fun getParameterForName(name: String) : Parameter<*>?{
        return paramsNameMap[name]
    }

    private fun getCommandForName(name: String) : Command<*>? {
        return commands[name]
    }

    fun isCommand(name: String) : Boolean {
        return commands.containsKey(name)
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

    fun parsed(command: String) : Boolean {
        return isParsed[joinedNameMap[command]] ?: false
    }

    class NotParsedException(notparsed: Parameter<*>) : Exception("$notparsed was not parsed!")
    class NoOptionException(notparsed: String) : Exception("$notparsed is not an available option!")
    class NoValueException(notparsed: Parameter<*>) : Exception("$notparsed has no value!")
    class IncorrectArityException(notparsed: Parameter<*>, arityFound: Int) : Exception("$notparsed needs ${notparsed.arity}, found $arityFound")
}