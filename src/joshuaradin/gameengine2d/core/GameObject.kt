package joshuaradin.gameengine2d.core

import joshuaradin.gameengine2d.standard.component.Transform
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSuperclassOf


class GameObject() {



    constructor(components: List<Component> = listOf()) : this(){
        this.components.addAll(components)
    }

    init {
        addComponent { Transform(it) }
    }

    var deactivateOnSceneChange: Boolean = true
    private val components: MutableList<Component> = mutableListOf()


    inline fun <reified T : Component> addComponent() : T?{
        return addComponent(T::class)
    }

    fun <T : Component> addComponent(type: KClass<out T>) : T?{
        return addComponent { type.createInstance() }
    }

    fun <T : Component> addComponent(factory: (GameObject) -> T?) : T? {
        val createdComponent: T? = factory(this)
        createdComponent?: return null
        addComponent(createdComponent)
        return createdComponent
    }

    fun addComponent(component: Component) : Component? {
        if(components.contains(component)) return null
        components.add(component)
        component.gameObject = this
        component.init()
        return component
    }

    inline fun <reified T : Component> getComponent() : T? {
        return getComponent(T::class)
    }

    fun <T : Component> getComponent(type: KClass<out T>): T? {
        for (component in components) {
            if(type.isSuperclassOf(component::class)) return component as? T
        }
        return null
    }

    inline fun <reified T : Component> getComponents() : Set<T>{
        return getComponents(T::class)
    }

    fun getAllComponents() : Set<ObjectBehavior>{
        return getComponents()
    }

    fun <T : Component> getComponents(type: KClass<T>) : Set<T> {
        val set: MutableSet<T> = mutableSetOf()
        for (component in components) {
            if(type.isSuperclassOf(component::class)) set.add(component as T)
        }
        return set
    }

    fun init(){
        for (component in components.filter { it.enabled }) {
            component.init()
        }
    }

    fun start() {
        for (component in  components.filter { it.enabled }) {
            component.start()
        }
    }

    fun update() {
        for (component in components.filter { it.enabled }) {
            component.update()
        }
    }
}