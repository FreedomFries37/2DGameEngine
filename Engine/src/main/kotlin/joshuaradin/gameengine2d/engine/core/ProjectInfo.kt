package joshuaradin.gameengine2d.engine.core

import joshuaradin.gameengine2d.engine.core.asset.Asset
import joshuaradin.gameengine2d.engine.core.basic.GameObject
import joshuaradin.gameengine2d.engine.core.scene.Scene
import joshuaradin.gameengine2d.engine.core.scene.SceneManager
import java.awt.Dimension

class ProjectInfo(var name: String) : Iterable<Pair<Scene, GameObject>> {

    val scenes = mutableListOf<Scene>()

    init {
        scenes.addAll(SceneManager.scenes)
    }

    var initialScene: Scene
        get() = scenes.first()
        set(value) {
            if(!scenes.contains(value)) return
            scenes.swap(scenes.first(), value)
        }

    val assets = mutableListOf<Asset<*>>()
    var windowSize = Dimension(1000, 800)


    fun toSceneGameObjectPairs() : Collection<Pair<Scene, GameObject>> {
        val output = mutableListOf<Pair<Scene, GameObject>>()
        for (scene in scenes) {
            output.addAll(scene.objectsInScene().map { Pair(scene, it) })
        }
        return output
    }

    /**
     * Returns an iterator over the elements of this object.
     */
    override fun iterator(): Iterator<Pair<Scene, GameObject>> {
        return toSceneGameObjectPairs().iterator()
    }
}

fun <T> MutableList<T>.swap(a: T, b: T){
    val aIndex = indexOf(a)
    val bIndex = indexOf(b)
    this[aIndex] = b

    this[bIndex] = a
}