package joshuaradin.gameengine2d.core

import joshuaradin.gameengine2d.core.asset.Asset
import joshuaradin.gameengine2d.core.scene.Scene
import joshuaradin.gameengine2d.standard.component.Camera2D
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
    var windowSize = Dimension(800, 500)
}

fun <T> MutableList<T>.swap(a: T, b: T){
    this[indexOf(a)] = b
    this[indexOf(b)] = a
}