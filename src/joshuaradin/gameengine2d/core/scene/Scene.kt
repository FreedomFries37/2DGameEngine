package joshuaradin.gameengine2d.core.scene

import joshuaradin.gameengine2d.core.basic.Component
import joshuaradin.gameengine2d.core.basic.GameObject
import joshuaradin.gameengine2d.core.service.GameObjectTracker
import joshuaradin.gameengine2d.standard.component.Camera2D
import java.io.Serializable
import kotlin.reflect.KClass

private var numScenes = 0
class Scene (val name: String = "scene" + (++numScenes)) : Serializable{

    class SceneNotInitializedException : Exception("Scene not initialized")

    private var baseObject: GameObject? = null
    var initialized: Boolean = false
        private set

    /**
     * Creates a [GameObject] and adds it to the base of the scene
     *
     * @param factory the factory for creating the object, default is an empty
     * @return returns the [GameObject] created
     */
    fun createGameObject(factory: () -> GameObject = { baseObject!!.createEmpty(baseObject)}) : GameObject {
        if(!initialized) throw SceneNotInitializedException()
        val gO: GameObject = factory()
        if(gO.parent != baseObject) gO.setParent(baseObject!!)
        return gO
    }

    fun objectsInScene() : List<GameObject> {
        if(!initialized) return listOf()
        return baseObject!!.getAllChildren()
    }

    fun initialize() {
        baseObject = GameObject.createEmpty(null, this)
        baseObject?.scene = this
        GameObjectTracker.instance.fixAssociatedScene(baseObject)
        initialized = true

        val camera = createGameObject()
        camera.name = "Camera"
        camera.addComponent<Camera2D>()


    }

    inline fun <reified T : Component> getGameObjectWithComponent() : GameObject? = getGameObjectWithComponent(T::class)

    fun <T : Component> getGameObjectWithComponent(type: KClass<T>) : GameObject? {
        return objectsInScene().find { it.hasComponent(type) }
    }

    inline fun <reified T> createQualifiedName() = createQualifiedName(T::class)

    fun createQualifiedName(clazz: KClass<*>) : String{
        return createQualifiedName(clazz.simpleName!!)
    }
    fun createQualifiedName(name: String) : String{
        if(!nameExists(name)) return name
        return name + (lastCreated(name)?.plus(1) ?: 1)
    }

    fun nameExists(name: String) : Boolean{
        return objectsInScene().any {it.name == name}
    }

    private fun lastCreated(name: String) : Int? {
        if(!nameExists(name + '1')) return null
        val nameRegex = "$name\\d+".toRegex()
        val filter = objectsInScene().filter { nameRegex.matches(it.name) }.map { it.name.removePrefix(name).toInt() }
        val max: Int = filter.maxBy { it }!!
        return  max
    }

    override fun toString(): String {
        return name
    }
}