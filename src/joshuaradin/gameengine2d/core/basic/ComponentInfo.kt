package joshuaradin.gameengine2d.core.basic

import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

class ComponentInfo<T : Component> (val component: T) : Iterable<Pair<String, *>>{

    val memberProperties = component::class.memberProperties.filter { it.visibility == KVisibility.PUBLIC }
    val properties = mutableMapOf<String, KProperty<*>>()
    val isMutable = mutableMapOf<String, Boolean>()

    init {
        for (memberProperty in memberProperties) {
            val name = memberProperty.name
            properties[name] = memberProperty
            isMutable[name] = memberProperty is KMutableProperty1<*, *>

        }
    }

    operator fun get(name: String) : Any? = properties[name]?.getter?.call()
    operator fun <R> set(name: String, value: R) {
        if(!properties.containsKey(name)) return
        if(isMutable[name] == false) return

        val mutable = properties[name] as? KMutableProperty1<T, R>?
        mutable?: return
        mutable.set(component, value)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ComponentInfo<*>) return false

        if (component != other.component) return false


        return true
    }

    override fun hashCode(): Int {
        return component.hashCode()
    }

    /**
     * Returns an iterator over the elements of this object.
     */
    override fun iterator(): Iterator<Pair<String, *>> {
        return properties.map { Pair(it.key, it.value.call(component)) }.iterator()
    }

    override fun toString(): String {
        return "[${component::class.simpleName}]\n${this.joinToString(separator = "\n", transform = {"${it.first} = ${it.second}"})}"
    }
}