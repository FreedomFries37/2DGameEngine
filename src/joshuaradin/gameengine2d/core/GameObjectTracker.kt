package joshuaradin.gameengine2d.core

import joshuaradin.gameengine2d.core.listeners.Event
import joshuaradin.gameengine2d.core.scene.Scene

class TrackerMissingException : Exception("Game Object Tracker Missing")

class GameObjectTracker (collection: Collection<Pair<Scene, GameObject>>){

    constructor(vararg objects: Pair<Scene, GameObject>) : this(objects.toList())


    private val objectSet: MutableSet<GameObject> = mutableSetOf()
    private val activeSceneToGameObjects = mutableMapOf<Scene, MutableSet<GameObject>>()
    private val _activeObjects: MutableSet<GameObject> = mutableSetOf()
    val activeObjects: Set<GameObject>
        get() {return _activeObjects; }
    private val startedObjects: MutableSet<GameObject> = mutableSetOf()

    init {
        for (pair in collection) {
            add(pair.second, pair.first)
        }

        for (gameObject in objectSet) {
            gameObject.init()
        }
    }

    fun add(gameObject: GameObject, activeScene: Scene) {
        if(!awareOfScene(activeScene)) addScene(activeScene)

        activeSceneToGameObjects[activeScene]?.add(gameObject)
        objectSet.add(gameObject)
        gameObject.init()
    }

    private fun addScene(scene: Scene) {
        val set = mutableSetOf<GameObject>()
        activeSceneToGameObjects.put(scene, set)
    }

    private fun awareOfScene(scene: Scene) : Boolean {
        return activeSceneToGameObjects.containsKey(scene)
    }

    fun sceneChange(scene: Scene){
        _activeObjects.removeIf {it.deactivateOnSceneChange}
        if(!awareOfScene(scene)) return
        _activeObjects.addAll(activeSceneToGameObjects[scene]!!.toList())
    }

    fun start() {
        for (activeObject in _activeObjects) {
            if(activeObject !in startedObjects) {
                startedObjects.add(activeObject)
                activeObject.start()
            }
        }
    }

    fun update() {
        for (activeObject in _activeObjects) {
            activeObject.update()
        }
    }

    fun GameObject.sendEvent(e: Event){
        for (c in this.getAllComponents()) {
            c.reactToEvent(e)
        }
    }


}