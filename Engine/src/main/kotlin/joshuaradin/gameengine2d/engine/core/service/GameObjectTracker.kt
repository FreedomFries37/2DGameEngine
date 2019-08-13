package joshuaradin.gameengine2d.engine.core.service

import joshuaradin.gameengine2d.engine.core.basic.GameObject
import joshuaradin.gameengine2d.engine.core.basic.GameObjectInfo
import joshuaradin.gameengine2d.engine.core.listeners.Event
import joshuaradin.gameengine2d.engine.core.scene.Scene
import joshuaradin.gameengine2d.engine.standard.component.InterferenceBoundary
import joshuaradin.gameengine2d.engine.standard.component.physics.Physics
import joshuaradin.gameengine2d.user.connecting.Time
import joshuaradin.gameengine2d.user.output.Renderer2DComponent

class TrackerMissingException : Exception("Game Object Tracker Missing")

class GameObjectTracker internal constructor (collection: Collection<Pair<Scene, GameObject>>){

    var editorMode = false
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
            else collection.filter { !_instance!!.objectSet.contains(it.second) }.forEach { instance.add(it.second, it.first) }
            return instance
        }
    }

    private val objectSet: MutableSet<GameObject> = mutableSetOf()
    private val activeSceneToGameObjects = mutableMapOf<Scene, MutableSet<GameObject>>()
    private val gameObjectToInitalScene = mutableMapOf<GameObject, Scene>()
    private val _activeObjects: MutableList<GameObject> = mutableListOf()
    val activeObjects: List<GameObject>
        get() {return _activeObjects; }
    private val startedObjects: MutableSet<GameObject> = mutableSetOf()


    init {
        for (pair in collection) {
            add(pair.second, pair.first)
            gameObjectToInitalScene[pair.second] = pair.first
        }

        if(!editorMode)
            for (gameObject in objectSet) {
                gameObject.awake()
            }
    }

    fun fixAssociatedScene(gameObject: GameObject?) {
        if(gameObject == null) return

        if(!gameObjectToInitalScene.containsKey(gameObject)){
            add(gameObject, gameObject.scene)
            return
        }
        val originalScene = gameObjectToInitalScene[gameObject]
        if (originalScene != gameObject.scene) {
            if(!awareOfScene(gameObject.scene)){
                addScene(gameObject.scene)
            }

            activeSceneToGameObjects[originalScene]?.remove(gameObject)
            activeSceneToGameObjects[gameObject.scene]?.add(gameObject)
            gameObjectToInitalScene[gameObject] = gameObject.scene
            if(gameObject.scene == currentScene) {
                _activeObjects.add(gameObject)
            }
        }
    }

    fun add(gameObject: GameObject, activeScene: Scene) {
        if(!awareOfScene(activeScene)) addScene(activeScene)

        activeSceneToGameObjects[activeScene]?.add(gameObject)
        objectSet.add(gameObject)
        gameObjectToInitalScene[gameObject] = activeScene
        gameObject.awake()
        if(gameObject.scene == currentScene) {
            _activeObjects.add(gameObject)
        }
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
        for (i in 0 until _activeObjects.size) {
            val activeObject = _activeObjects[i]
            if(!activeObject.started) {
                startedObjects.add(activeObject)
                activeObject.started = true
                activeObject.start()



            }
        }
        printLayeredToStringInfo()
    }

    fun update() {
        for (activeObject in _activeObjects) {
            activeObject.update()
        }
    }

    fun fixedUpdate() {
        Physics.updatePhysics()
        for (activeObject in _activeObjects) {
            activeObject.fixedUpdate()
        }
    }

    fun getRenderables() : List<Renderer2DComponent> {
        val map = activeObjects.filter { it.hasComponent<Renderer2DComponent>() }
            .map { it.getComponents(Renderer2DComponent::class) }
        val flatten = map.flatten().sortedBy { it.level }
        return flatten
    }

    fun colliders() : List<InterferenceBoundary> {
        val map = activeObjects.filter { it.hasComponent<InterferenceBoundary>()}
            .map { it.getComponents(InterferenceBoundary::class) }
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

    fun GameObject.level() : Int {
        var output = 0
        if(parent != null) output += parent!!.level() + 1
        return output
    }

    fun printAllObjectSceneAndPosition() {
        println("Current Running Time = " + "%.2f sec".format(Time.totalTime))
        for (scene in activeSceneToGameObjects.keys) {
            println("#${scene.name}")
            for (gameObject in activeSceneToGameObjects[scene]!!) {
                print("\t" + "  ".repeat(gameObject.level()))
                println("${gameObject.name} - " + gameObject.transform.position + " @ ${gameObject.transform.rotation} [global: ${gameObject.getGlobalPosition()}]")
            }
        }
    }

    fun printLayeredToStringInfo() {
        println("Current Running Time = " + "%.2f sec".format(Time.totalTime))
        for (scene in activeSceneToGameObjects.keys) {
            println("#${scene.name}")
            val parentSortedList = activeSceneToGameObjects[scene]!!.toParentSortedList()
            for (gameObject in parentSortedList) {
                print("\t" + "  ".repeat(gameObject.level()))
                if(gameObject.enabled) {
                    val hasComponent = gameObject.hasComponent<Renderer2DComponent>()
                    if(gameObject.started && hasComponent){
                        print("[#] ")
                    }else if(hasComponent) {
                        print("[-] ")
                    }else print("[ ] ")
                } else print("    ")

                println("$gameObject")
                for (component in gameObject.getAllComponents()) {
                    print("\t    " + "  ".repeat(gameObject.level()) + " +")
                    println("[${component.javaClass.simpleName}] " + component)
                }
            }
        }
    }

    fun Set<GameObject>.toParentSortedList() : List<GameObject> {
        val visited = mutableSetOf<GameObject>()
        val output = mutableListOf<GameObject>()
        for (gameObject in this) {
            if(!visited.contains(gameObject)) {
                val found = listOf(gameObject, *gameObject.getAllChildren().toTypedArray())
                output.addAll(found)
                visited.addAll(found)
            }
        }
        return output
    }

    fun printFullInfo() {
        for (gameObject in objectSet) {
            GameObjectInfo(gameObject).printInfo()
        }
    }

}