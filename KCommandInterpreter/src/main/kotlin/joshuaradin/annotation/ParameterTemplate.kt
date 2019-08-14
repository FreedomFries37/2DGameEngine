package joshuaradin.annotation

import joshuaradin.core.DefaultConverters
import joshuaradin.core.IStringConverter
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class ParameterTemplate (val additionalNames: Array<String> = [],
                                    val required: Boolean = false,
                                    val arity: Int = 1,
                                    val converter: KClass<out IStringConverter<*>> = DefaultConverters.StringConverter::class)