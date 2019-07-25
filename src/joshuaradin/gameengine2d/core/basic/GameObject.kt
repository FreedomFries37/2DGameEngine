package joshuaradin.gameengine2d.core.basic

import joshuaradin.gameengine2d.core.scene.Scene
import joshuaradin.gameengine2d.core.scene.SceneManager
import joshuaradin.gameengine2d.core.service.ComponentCloner
import joshuaradin.gameengine2d.core.service.GameObjectTracker
import joshuaradin.gameengine2d.standard.component.Transform
import joshuaradin.gameengine2d.standard.type.Vector2
import java.io.IOException
import java.io.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.memberProperties


class GameObject(scene: Scene, private var _parent: GameObject?, children: List<GameObject>, name: String = scene.createQualifiedName<GameObject>()) : EngineIntractable(), Serializable {
    private var _name = scene.createQualifiedName(name)
    var name: String
        get() =_name
        set(value) {
            _name = scene.createQualifiedName(value)
        }
    var started: Boolean = false
    val parent: GameObject?
        get() = _parent
    private var _scene = scene
    var scene: Scene
        get() = _scene
        set(value) {
            if(parent == null || parent?.scene == value){
                _scene = value
            }
        }

    companion object {
        fun createEmpty(parent: GameObject?) : GameObject {

            val name: String = if(parent == null) "Base" else "Empty"

            return if (parent != null) {
                val initialScene = parent.scene
                GameObject(initialScene, parent, listOf(), name)
            } else GameObject(SceneManager.stagingScene, parent, listOf(), name)
        }

        fun instantiate(other: GameObject) : GameObject {
            val created: GameObject = createEmpty(other.parent)
            created.name = other.name + " copy"
            for (component in other.components) {
                val deepClone = ComponentCloner().deepClone(component)
                if(deepClone == null) throw IOException()
                created.addComponent(deepClone)
                println(deepClone)
            }

            return created
        }

        private inline fun <reified T : Component> loopInternal(created: GameObject, component: T) {
            val createdComponent = created.addComponent(component::class)
            if (createdComponent != null) component.applyTo(createdComponent)
        }

        inline fun <reified T : Component> T.applyTo(other: Component) : Component? {
            val t: T? = other as? T
            return null ?: this.applyTo(T::class, t!!)
        }


        fun <T : Component> T.applyTo(type: KClass<T>, other: T) : Component {
            for (memberProperty in type.memberProperties.filter { it.visibility == KVisibility.PUBLIC }) {

                if(memberProperty is KMutableProperty<*>) {
                    val thisVal = memberProperty.getter.call(this)
                    memberProperty.setter.call(other, thisVal)
                }
            }

            return other
        }
    }




    private val children: MutableList<GameObject> = children.toMutableList()
    var deactivateOnSceneChange: Boolean = true
    private val components: MutableList<Component> = mutableListOf()



    init {
        GameObjectTracker.instance?.add(this, scene)
        parent?.children?.add(this)
        for (child in children) {
            child.setParent(this)
        }
        addComponent<Transform>()
    }

    val transform = getComponent<Transform>()!!





    fun addChild(o: GameObject) : Boolean {
        if(o in children) return false
        children.add(o)
        o._parent?.children?.remove(o)
        o._parent = this
        return true
    }

    fun addChildren(os: Collection<GameObject>){
        os.filter { it !in children }.forEach { addChild(it)}
    }

    fun removeChild(o: GameObject) : GameObject? {
        if(o !in children) return null
        children.remove(o)
        o._parent = null
        return o
    }

    fun removeChildren(os: List<GameObject>){
        os.filter { it in children }.forEach { removeChild(it)}
    }

    fun setParent(p: GameObject) {
        this.transform.position = getGlobalPosition()
        parent?.removeChild(this)
        p.addChild(this)
        this.transform.applyGlobalToLocal(p.getGlobalPosition())
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

    fun getAllChildren() : List<GameObject> {
        return getChildren(Int.MAX_VALUE)
    }

    fun createEmpty(parent: GameObject? = this) : GameObject {
        return GameObject.createEmpty(parent)
    }

    fun instantiate(other: GameObject, parent: GameObject = this) : GameObject {
        val created: GameObject = createEmpty(parent)
        created.name = other.name + " copy"
        for (component in other.components) {
            if(component is Transform){
                created.transform.position = component.position
                created.transform.rotation = component.rotation
                created.transform.scale = component.scale
            }else {
                val deepClone = ComponentCloner().deepClone(component)
                if (deepClone == null) throw IOException()
                created.addComponent(deepClone)
                println(deepClone)
            }
        }
        created.init()
        if(started) created.start()
        GameObjectTracker.instance.printAllObjectSceneAndPosition()
        return created
    }


    inline fun <reified T : Component> addComponent() : T?{
        return addComponent(T::class)
    }

    fun <T : Component> addComponent(type: KClass<out T>) : T?{
        return addComponent { type.createInstance() }
    }

    fun <T : Component> addComponent(factory: () -> T?) : T? {
        val createdComponent: T? = factory()
        createdComponent?: return null
        addComponent(createdComponent)
        return createdComponent
    }

    fun addComponent(component: Component) : Component? {
        if(components.contains(component)) return null
        components.add(component)
        component.gameObject = this
        component.init()
        if(started){
            component.start()
        }
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

    override fun toString(): String {
        return name
    }

    fun getGlobalPosition() : Vector2 {
        var output: Vector2 = transform.position
        if(parent != null) output += parent!!.getGlobalPosition()
        return output
    }

    fun getDistanceTo(other: GameObject) : Vector2 {
        return getGlobalPosition() - other.getGlobalPosition()
    }

    fun getDistanceTo(other: Transform) : Vector2 {
        return getDistanceTo(other.gameObject!!)
    }



    override fun init(){
        for (component in components.filter { it.enabled }) {
            component.init()
        }
    }

    override fun start() {
        for (component in  components.filter { it.enabled }) {
            component.start()
        }
    }

    override fun update() {
        for (component in components.filter { it.enabled }) {
            component.update()
        }
    }

    override fun onBoundaryEnter(other: GameObject){
        for (component in  components.filter { it.enabled }) {
            component.onBoundaryEnter(other)
        }
    }

    override fun onBoundaryStay(other: GameObject){
        for (component in  components.filter { it.enabled }) {
            component.onBoundaryStay(other)
        }
    }

    override fun onBoundaryExit(other: GameObject){
        for (component in  components.filter { it.enabled }) {
            component.onBoundaryExit(other)
        }
    }

    override fun onMouseClick() {
        for (component in  components.filter { it.enabled }) {
            component.onMouseClick()
        }
    }

    override fun onMouseDown() {
        for (component in  components.filter { it.enabled }) {
            component.onMouseDown()
        }}

    override fun onMouseUp() {
        for (component in  components.filter { it.enabled }) {
            component.onMouseUp()
        }
    }

    override fun onMouseStay() {
        for (component in  components.filter { it.enabled }) {
            component.onMouseStay()
        }
    }

    override fun onMouseExit() {
        for (component in  components.filter { it.enabled }) {
            component.onMouseExit()
        }
    }
}