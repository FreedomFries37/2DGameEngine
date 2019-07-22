package joshuaradin.gameengine2d.core

import joshuaradin.gameengine2d.standard.component.Transform
import kotlin.reflect.KClass

class GameObjectSetException : Exception("Can't reassign game object")

abstract class Component {

    private var gameObjectSet = false
    private var _gameObject: GameObject? = null

    var enabled: Boolean = true

    var gameObject: GameObject?
        get() = _gameObject
        set(value) {
            if(gameObjectSet) {
                throw GameObjectSetException()
            }
            gameObjectSet = true
            _gameObject = value
        }

    val transform: Transform?
        get() {return gameObject?.getComponent()}

    abstract fun init()
    abstract fun start()
    abstract fun update()



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



}