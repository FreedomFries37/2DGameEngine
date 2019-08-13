package joshuaradin.core



class Command<out T> (
    names: Array<String>,
    arity: Int,
    val separator: String,
    val parameters: Collection<Parameter<*>>,
    val cmdConverter: IMultiParameterConverter<*>
) : Parameter<T?>(names, arity, false, {throw IllegalArgumentException()})

