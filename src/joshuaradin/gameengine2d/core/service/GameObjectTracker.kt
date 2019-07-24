package joshuaradin.gameengine2d.core.service

import joshuaradin.gameengine2d.core.basic.GameObject
import joshuaradin.gameengine2d.core.listeners.Event
import joshuaradin.gameengine2d.core.scene.Scene
import joshuaradin.gameengine2d.standard.component.InterferenceBoundry
import joshuaradin.gameengine2d.user.output.Renderer2DComponent

class TrackerMissingException : Exception("Game Object Tracker Missing")

class GameObjectTracker private constructor (collection: Collection<Pair<Scene, GameObject>>){

    var currentScene: Scene? = null
        private set
    private constructor(objects: Array<Pair<Scene, GameObject>>) : this(objects.toList())

    companion object{
        private var _instance: GameObjectTracker? = null
        val instance: GameObjectTracker
            get(){
                if (_instance == null) _instance =
                    initialize()
                return _instance!!
            }


        fun initialize(vararg objects: Pair<Scene, GameObject>) : GameObjectTracker {

            if(_instance == null) _instance =
                GameObjectTracker(objects.toList())
            else objects.forEach { instance.add(it.second, it.first) }
            return instance
        }

        fun initialize(collection: Collection<Pair<Scene, GameObject>>) : GameObjectTracker {
            if(_instance == null) _instance =
                GameObjectTracker(collection)
            else collection.forEach { instance.add(it.second, it.first) }
            return instance
        }
    }

    private val objectSet: MutableSet<GameObject> = mutableSetOf()
    private val activeSceneToGameObjects = mutableMapOf<Scene, MutableSet<GameObject>>()
    private val gameObjectToInitalScene = mutableMapOf<GameObject, Scene>()
    private val _activeObjects: MutableSet<GameObject> = mutableSetOf()
    val activeObjects: Set<GameObject>
        get() {return _activeObjects; }
    private val startedObjects: MutableSet<GameObject> = mutableSetOf()


    init {
        for (pair in collection) {
            add(pair.second, pair.first)
            gameObjectToInitalScene[pair.second] = pair.first
        }

        for (gameObject in objectSet) {
            gameObject.init()
        }
    }

    fun add(gameObject: GameObject, activeScene: Scene) {
        if(!awareOfScene(activeScene)) addScene(activeScene)

        activeSceneToGameObjects[activeScene]?.add(gameObject)
        objectSet.add(gameObject)
        gameObjectToInitalScene[gameObject] = activeScene
        gameObject.init()
    }

    fun addScene(scene: Scene) {
        val set = mutableSetOf<GameObject>()
        activeSceneToGameObjects.put(scene, set)
    }

    fun awareOfScene(scene: Scene) : Boolean {
        return activeSceneToGameObjects.containsKey(scene)
    }

    fun getInitialScene(o: GameObject?) : Scene?{
        return gameObjectToInitalScene[o]
    }

    fun sceneChange(scene: Scene){
        _activeObjects.removeIf {it.deactivateOnSceneChange}
        if(!awareOfScene(scene)) return
        _activeObjects.addAll(activeSceneToGameObjects[scene]!!.toList())
        currentScene = scene
        start()
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

    fun getRenderables() : List<Renderer2DComponent> {
        val map = activeObjects.filter { it.hasComponent<Renderer2DComponent>() }
            .map { it.getComponents(Renderer2DComponent::class) }
        val flatten = map.flatten().sortedBy { it.level }
        return flatten
    }

    fun colliders() : List<InterferenceBoundry> {
        val map = activeObjects.filter { it.hasComponent<InterferenceBoundry>()}
            .map { it.getComponents(InterferenceBoundry::class) }
        val flattened = map.flatten().toMutableList()
        flattened.removeIf { it.isTrigger }
        return flattened
    }

    fun GameObject.sendEvent(e: Event){
        for (c in this.getAllComponents()) {
            c.reactToEvent(e)
        }
    }

    fun find(s: String) : GameObject? {
        return _activeObjects.find { it.name == s }
    }

    fun printAllObjectSceneAndPosition() {
        for (scene in activeSceneToGameObjects.keys) {
            println("#${scene.name}")
            for (gameObject in activeSceneToGameObjects[scene]!!) {
                println("\t${gameObject.name} - " + gameObject.transform.position + " @ ${gameObject.transform.rotation} [global: ${gameObject.getGlobalPosition()}]")
            }
        }
    }

}