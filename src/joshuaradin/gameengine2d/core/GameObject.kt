package joshuaradin.gameengine2d.core

import joshuaradin.gameengine2d.standard.component.Transform
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSuperclassOf


class GameObject(var parent: GameObject?, children: List<GameObject>) {



    companion object {
        fun createEmpty(parent: GameObject?) : GameObject {
            return GameObject(parent, istOf())
        }
    }


    constructor(parent: GameObject, components: List<Component>, children: MutableList<GameObject>) : this(parent, children){
        this.components.addAll(components)
    }



    private val children: MutableList<GameObject> = children.toMutableList()
    var deactivateOnSceneChange: Boolean = true
    private val components: MutableList<Component> = mutableListOf()

    init {
        for (child in children) {
            child.parent = this
        }
        addComponent<Transform>()
    }


    fun addChild(o: GameObject) : Boolean {
        if(o in children) return false
        children.add(o)
        o.parent = this
        return true
    }

    fun addChildren(os: Collection<GameObject>){
        os.filter { it !in children }.forEach { addChild(it)}
    }

    fun removeChild(o: GameObject) : GameObject? {
        if(o !in children) return null
        children.remove(o)
        o.parent = null
        return o
    }

    fun removeChildren(os: List<GameObject>){
        os.filter { it in children }.forEach { removeChild(it)}
    }

    fun changeParent(p: GameObject) {
        parent?.removeChild(this)
        p.addChild(p)
    }

    /**
     *
     * @param levels the amount of levels to search for children
     *          0 = no search
     *          1 (default value) = only direct children
     */
    fun getChildren(levels: Int = 1) : List<GameObject>{
        if(levels <= 0) return listOf()
        val output = mutableListOf<GameObject>()
        for (child in children) {
            output.add(child)
            output.addAll(child.getChildren(levels - 1))
        }
        return output
    }




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

    fun <T : Component> hasComponent(type: KClass<out T>): Boolean {
        return getComponent(type) != null
    }

    inline fun <reified T : Component> hasComponent() : Boolean {
        return hasComponent(T::class)
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