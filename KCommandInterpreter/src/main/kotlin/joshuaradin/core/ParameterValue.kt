package joshuaradin.core

data class ParameterValue<T>(val parameter: Parameter<T>, val value: T?)

fun <T> Parameter<T>.toValue(vararg strings: String) : ParameterValue<T>{
    return ParameterValue(this, this.converter(strings.asList()))
}