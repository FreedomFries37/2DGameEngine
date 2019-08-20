package joshuaradin.core

import java.io.InvalidClassException
import kotlin.reflect.KClass


open class Parameter<out T> (

    val names: Array<String>,
    val arity: Int,
    val required: Boolean,
    val converter: (List<String>) -> T?,
    val defaultValue: T? = null
) : Cloneable {

    var priority: Int = 1
    var layout: String
        protected set
    var description: String = ""

    init {

        var layout = names.first()
        for (i in 0 until arity) {
            layout += " o$i"
        }



        this.layout = layout
    }

    override fun clone(): Parameter<T> {
        return Parameter(names, arity, required, converter, defaultValue)
    }

    fun default() : ParameterValue<T> {
        return ParameterValue(this, defaultValue)
    }


    override fun toString(): String {
        return "Parameter(${names.joinToString()}) [arity=$arity, required=$required, defaultValue=$defaultValue]"
    }

    open fun printInfo() {

        println(infoString())
    }


    fun infoString() : String{
        val joinToString = names.joinToString()
        return "$joinToString\t\t-\t$layout\n${" ".repeat(joinToString.length)}\t\t \t$description"

    }

    companion object {


        private val converters: MutableMap<KClass<*>, IStringConverter<*>> = mutableMapOf()

        fun <T : Any> addConverter(type: KClass<out T>, converter: IStringConverter<T>) {
            converters.putIfAbsent(type, converter)
        }

        inline fun <reified T : Any> addConverter(converter: IStringConverter<T>) = addConverter(T::class, converter)

        inline fun <reified T : Any> createInstance(names: Array<String>,
                                                    arity: Int,
                                                    required: Boolean,
                                                    defaultValue: T? = null) : Parameter<T> = createInstance(names, arity, required, defaultValue, T::class)
        fun <T : Any> createInstance(names: Array<String>,
                                     arity: Int,
                                     required: Boolean,
                                     defaultValue: T? = null,
                                     type: KClass<out T>
        ) : Parameter<T> {
            return when(type) {
                String::class -> Parameter(names, arity, required, DefaultConverters.StringConverter(), defaultValue) as Parameter<T>
                Int::class -> Parameter(names, arity, required, DefaultConverters.IntConverter(), defaultValue) as Parameter<T>

                in converters.keys -> {
                    val converter = converters[type] as? IStringConverter<T>
                    converter?: throw InvalidClassException("Incorrect Type")
                    Parameter(names, arity, required, converter, defaultValue)
                }
                else -> {
                    throw InvalidClassException("No default converter")
                }
            }
        }

    }


}