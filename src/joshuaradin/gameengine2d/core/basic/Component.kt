package joshuaradin.gameengine2d.core.basic

import joshuaradin.gameengine2d.standard.component.Transform
import java.io.Serializable
import kotlin.reflect.KClass

class GameObjectSetException : Exception("Can't reassign game object")


abstract class Component : Cloneable, Serializable{




    private var _gameObject: GameObject? = null


    var gameObject: GameObject?
        get() = _gameObject
        set(value) {
            if(gameObjectSet) {
                return
            }
            gameObjectSet = true
            _gameObject = value
        }
    var enabled: Boolean = true
    private var gameObjectSet = false


    val transform: Transform?
        get() {return gameObject?.getComponent()}

    abstract fun init()
    abstract fun start()
    abstract fun update()
    abstract fun onBoundaryEnter(other: GameObject)
    abstract fun onBoundaryStay(other: GameObject)
    abstract fun onBoundaryExit(other: GameObject)

    inline fun <reified T : Component> addComponent() : T? {
        return gameObject?.addComponent()
    }

    fun <T : Component> addComponent(type: KClass<T>) : T? {
        return gameObject?.addComponent(type)
    }

    inline fun <reified T : Component> getComponent() : T? {
        return gameObject?.getComponent()
    }

    fun <T : Component> getComponent(type: KClass<T>) : T? {
        return gameObject?.addComponent(type)
    }

    public override fun clone(): Any {
        return super.clone()
    }



}