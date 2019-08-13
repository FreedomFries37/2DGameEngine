package joshuaradin.core


open class Parameter<out T> (

    val names: Array<String>,
    val arity: Int,
    val required: Boolean,
    val converter: (List<String>) -> T?,
    val defaultValue: T? = null
)