package joshuaradin.gameengine2d.core.service

import joshuaradin.gameengine2d.core.basic.Component
import java.io.*


class ComponentCloner{

    fun <T : Component>  deepClone(obj: T) : T? {

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
}