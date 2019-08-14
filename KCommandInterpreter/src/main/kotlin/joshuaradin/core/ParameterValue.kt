package joshuaradin.core

data class ParameterValue<out T>(val parameter: Parameter<T>, val value: T?)

fun <T> Parameter<T>.toValue(strings: List<String>) : ParameterValue<T>{
    return ParameterValue(this, this.converter(strings))
}