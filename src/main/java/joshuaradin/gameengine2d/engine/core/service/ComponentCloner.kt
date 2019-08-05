package joshuaradin.gameengine2d.engine.core.service

import joshuaradin.gameengine2d.engine.core.basic.Component
import joshuaradin.gameengine2d.engine.core.basic.GameObject
import joshuaradin.gameengine2d.engine.standard.component.Transform
import java.io.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties


object ComponentCloner{


    /**
     * Works anywhere
     */
    fun <T : Component>  serializationClone(obj: T) : T? {

        return try {
            val baos = ByteArrayOutputStream()
            val oos = ObjectOutputStream(baos)
            oos.writeObject(obj)

            val bais = ByteArrayInputStream(baos.toByteArray())
            val ois = ObjectInputStream(bais)
            ois.readObject() as? T?
        } catch (e: IOException) {
            e.printStackTrace()
            obj.clone() as? T?
        }

    }

    /**
     *
     */
    fun GameObject.cloneGameObject() : GameObject? {
        val output = this.createEmpty(this.parent)
        output.name = name

        for (child in getChildren()) {
            child.cloneGameObject()
        }

        for (i in 0 until numComponents) {
            serializableFieldsClone(i, this, output)
        }



        return output
    }

    private fun serializableFieldsClone(index: Int, original: GameObject, gameObject: GameObject) {
        serializableFieldsClone(original.getComponentReference(index).comp, gameObject)
    }

    private inline fun <reified T : Component> serializableFieldsClone(obj : T, gameObject: GameObject)
            = serializableFieldsClone(obj::class, obj, gameObject)


    private fun <T : Component> serializableFieldsClone(type: KClass<out T>, obj : T, gameObject: GameObject){
        val clone = if(type != Transform::class) gameObject.addComponent(type) else gameObject.transform

        val memberProperties = type.memberProperties
        val mutableProperties = memberProperties.filter { it is KMutableProperty1<*, *>}
        val publicProperties = mutableProperties.filter { it.visibility?.equals(KVisibility.PUBLIC) ?: false}
        // serializableProperties = mutableProperties.filter {it.typeParameters[1] is Serializable}
        for (memberProperty in publicProperties.map { it as KMutableProperty1<*, *> }) {
            val gotten = memberProperty.getter.call(obj)
            memberProperty.setter.call(clone, gotten)
        }


    }
}