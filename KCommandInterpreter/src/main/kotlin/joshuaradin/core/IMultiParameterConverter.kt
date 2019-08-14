package joshuaradin.core

interface IMultiParameterConverter<out T> : (Set<ParameterValue<*>>) -> T{

    fun convert(strs: Set<ParameterValue<*>>) : T
    override fun invoke(p1: Set<ParameterValue<*>>): T {
        return convert(p1)
    }
}