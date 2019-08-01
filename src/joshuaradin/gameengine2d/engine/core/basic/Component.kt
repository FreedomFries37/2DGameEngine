package joshuaradin.gameengine2d.engine.core.basic

import joshuaradin.gameengine2d.engine.standard.component.Transform
import java.io.Serializable
import kotlin.reflect.KClass

class GameObjectSetException : Exception("Can't reassign game object")


abstract class Component : EngineIntractable(), Cloneable, Serializable{




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
    private var gameObjectSet = false


    val transform: Transform?
        get() {return gameObject?.getComponent()}


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

class ComponentReference<T : Component> (val comp: T)