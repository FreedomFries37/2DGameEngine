package joshuaradin.gameengine2d.core.scene

import joshuaradin.gameengine2d.core.GameObject


class Scene (val name: String = "scene" + (++numScenes)) {
    companion object{
        var numScenes = 0
    }

    val baseObject: GameObject

    init {
        baseObject =
    }
}