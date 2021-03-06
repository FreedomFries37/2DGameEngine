package joshuaradin.gameengine2d.engine.core.scene

import joshuaradin.gameengine2d.engine.core.basic.GameObject
import joshuaradin.gameengine2d.engine.core.scene.SceneManager.addScene
import joshuaradin.gameengine2d.engine.core.service.GameObjectTracker
import joshuaradin.gameengine2d.engine.standard.type.Vector2
import joshuaradin.gameengine2d.engine.standard.type.geometry.Rotation

object SceneManager {

    val _scenes = mutableListOf<Scene>()
    val scenes: List<Scene>
        get() = _scenes

    var activeScene: Scene? = null
        private set
    val stagingScene : Scene

    init {
        stagingScene = Scene("&staging")
        addScene(stagingScene)
    }


    fun addScene(name: String? = null) : Scene? {
        val scene = if(name.isNullOrBlank()) Scene() else Scene(name)

        return addScene(scene)
    }

    fun addScene(s: Scene) : Scene? {
        if(_scenes.filter { it.name == s.name }.isNotEmpty()) return null
        if(activeScene == null) activeScene = s
        _scenes.add(s)
        s.initialize()
        return s
    }

    fun sceneChange(s: Scene) {
        if(!scenes.contains(s)) addScene(s)
        activeScene = s
        GameObjectTracker.instance.sceneChange(s)
    }

    fun getScene(i: Int) : Scene {
        return _scenes[i]
    }

    fun getScene(s: String) : Scene? {
        return _scenes.find { it.name == s }
    }

    fun initGameObjectTracker() : GameObjectTracker {
        val collection = mutableSetOf<Pair<Scene, GameObject>>()
        for (scene in scenes) {

            val objectsInScene = scene.objectsInScene()

            for (gameObject in objectsInScene) {
                val pair = Pair(scene, gameObject)
                collection.add(pair)
            }

        }

        val output = GameObjectTracker.initialize(collection)
        for (scene in scenes) {
            if(!output.awareOfScene(scene)) output.addScene(scene)
        }

        return output
    }


}

fun main() {

    val baseScene = addScene()!!
    val gO1 = baseScene.createGameObject()
    val gO2 = baseScene.createGameObject()

    gO2.setParent(gO1)
    gO2.transform.position += Vector2(10.0, 0.0)
    gO2.transform.rotation = Rotation.createDeg(45.0)

    gO1.transform.rotation += Rotation.createDeg(-90.0)
    GameObjectTracker.instance.printAllObjectSceneAndPosition()

    gO1.transform.position += Vector2(15.0, 0.0)
    GameObjectTracker.instance.printAllObjectSceneAndPosition()

    gO1.transform.rotation += Rotation.createDeg(45.0)
    GameObjectTracker.instance.printAllObjectSceneAndPosition()

}