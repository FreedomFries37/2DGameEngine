package joshuaradin.annotation

import joshuaradin.core.DefaultConverters
import joshuaradin.core.IMultiParameterConverter
import joshuaradin.core.IStringConverter
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class CommandTemplate (val names: Array<String> = [],
                                  val arity: Int = 0,
                                  val seperator: String = "",
                                  val converter: KClass<out IMultiParameterConverter<*>> = DefaultConverters.DefaultCommandConverter::class,
                                  val requiredOptionsConverter: KClass<out IStringConverter<*>> = DefaultConverters.StringConverter::class
)