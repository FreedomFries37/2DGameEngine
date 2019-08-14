package joshuaradin.core



class Command<out T> (
    names: Array<String>,
    arity: Int,
    val separator: String,
    parameters: Collection<Parameter<*>>,
    val cmdConverter: IMultiParameterConverter<*>,
    requiredOptionsConverter: IStringConverter<*> = DefaultConverters.StringConverter()
) : Parameter<T?>(names, arity, false, {throw IllegalArgumentException()}) {

    companion object {
        val defaultName = arrayOf("default")
    }

    private val paramMap: MutableMap<Parameter<*>, Any?>  = mutableMapOf()
    private val paramsNameMap: Map<String, Parameter<*>>
    val parameters: Collection<Parameter<*>>

    init {

        val defaultParameter = Parameter(defaultName, arity, arity != 0, requiredOptionsConverter)
        this.parameters = parameters.union(listOf(defaultParameter))
        for (parameter in this.parameters) {
            paramMap[parameter] = null
        }
        paramsNameMap = mapOf(*(this.parameters.map {
            val param = it
            it.names.map {
                Pair(it, param)
            }
        }.flatten().toTypedArray()))

    }


    internal fun getParameterForName(name: String) : Parameter<*>?{
        if(!paramsNameMap.containsKey(name)) throw CommandInterpreter.NoOptionException(name)
        return paramsNameMap[name]
    }

    internal fun getParameterForNames(name: Array< out String>) : Parameter<*>?{
        for (s in name) {
            val parameterForName = getParameterForName(s)
            if(parameterForName != null) return parameterForName
        }

        return null
    }



    fun <T> getParameterValue(parameter: String) : T? {
        val param = getParameterForName(parameter)
        param?: return null
        return paramMap[param] as? T?
    }

    fun <T> setParameterValue(parameter: String, value: T?) {
        paramMap[getParameterForName(parameter)!!] = value
    }

    override fun toString(): String {
        return "Command(parameters=$parameters)"
    }


}
