package joshuaradin.core

interface IMultiParameterConverter<out T> : (Set<ParameterValue<*>>) -> T{

    fun convert(strs: Set<ParameterValue<*>>) : T
    override fun invoke(p1: Set<ParameterValue<*>>): T {
        return convert(p1)
    }

    fun <T> Set<ParameterValue<*>>.get(param: String) : T {
        return this.find { it.parameter.names.contains(param) }?.value as T
    }

    fun <T> Set<ParameterValue<*>>.get(param: Array<String>) : T {
        return this.find { it.parameter.names.intersect(param.toList()).isNotEmpty() }?.value as T
    }

    fun <T> Set<ParameterValue<*>>.getDefault() : T = get(Command.defaultName)
}