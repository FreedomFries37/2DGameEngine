package joshuaradin.annotation

import joshuaradin.core.Command
import joshuaradin.core.IMultiParameterConverter
import joshuaradin.core.IStringConverter
import joshuaradin.core.Parameter
import java.io.InvalidClassException
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

object TemplateConverter {

    private fun <T> convertParameter(obj: KProperty<T>, parentCmd: Any) : Parameter<T> {
        val parameterTemplate = obj.findAnnotation<ParameterTemplate>()
        parameterTemplate?: throw NullPointerException()
        val name = obj.name
        val allNames = if(parameterTemplate.additionalNames.isEmpty()) arrayOf(name) else parameterTemplate.additionalNames


        val converter = parameterTemplate.converter.createInstance() as IStringConverter<T>


        val defaultValue = obj.getter.call(parentCmd)


        val parameter =
            Parameter(allNames, parameterTemplate.arity, parameterTemplate.required, converter, defaultValue)
        parameter.priority = parameterTemplate.priority
        return parameter
    }

    inline fun <reified T : Any, R> convertCommand() = convertCommand<T,R>(T::class)
    inline fun <reified T : Any> convertCommandDef() = convertCommand<T,Any?>(T::class)

    fun <T : Any, R> convertCommand(type: KClass<out T>) : Command<R> {
        val commandTemplate = type.findAnnotation<CommandTemplate>()
        commandTemplate?: throw InvalidClassException("Doesn't contain command template")
        val parameters = mutableSetOf<Parameter<*>>()
        val name = type.simpleName!!
        val allNames = listOf(name).union(commandTemplate.names.toList()).toTypedArray()

        val instance = type.createInstance()
        for (property in type.memberProperties.filter { it.visibility == KVisibility.PUBLIC && it.findAnnotation<ParameterTemplate>() != null}) {

            val parameter = convertParameter(property, instance)
            parameters.add(parameter)
        }

        val command = Command<R>(
            allNames,
            commandTemplate.arity,
            commandTemplate.seperator,
            parameters,
            commandTemplate.converter.createInstance() as IMultiParameterConverter<R>,
            commandTemplate.requiredOptionsConverter.createInstance()
        )
        command.tag = if(commandTemplate.tag.isNotBlank()) commandTemplate.tag else null
        return command
    }
}