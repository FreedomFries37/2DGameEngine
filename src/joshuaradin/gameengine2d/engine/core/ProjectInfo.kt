package joshuaradin.gameengine2d.engine.core

import joshuaradin.gameengine2d.engine.core.asset.Asset
import joshuaradin.gameengine2d.engine.core.scene.Scene
import java.awt.Dimension

class ProjectInfo(var name: String) {

    val scenes = mutableListOf<Scene>()

    var initialScene: Scene
        get() = scenes.first()
        set(value) {
            if(!scenes.contains(value)) return
            scenes.swap(scenes.first(), value)
        }

    val assets = mutableListOf<Asset<*>>()
    var windowSize = Dimension(1000, 800)
}

fun <T> MutableList<T>.swap(a: T, b: T){
    this[indexOf(a)] = b
    this[indexOf(b)] = a
}