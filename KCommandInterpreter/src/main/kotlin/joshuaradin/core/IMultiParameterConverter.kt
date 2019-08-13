package joshuaradin.core

interface IMultiParameterConverter<out T> : (List<ParameterValue<*>>) -> T{

    fun convert(strs: List<ParameterValue<*>>) : T
    override fun invoke(p1: List<ParameterValue<*>>): T {
        return convert(p1)
    }
}