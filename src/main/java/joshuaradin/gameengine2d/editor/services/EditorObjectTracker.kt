package joshuaradin.gameengine2d.editor.services

import joshuaradin.gameengine2d.editor.gui.RunInEditor
import joshuaradin.gameengine2d.engine.core.basic.EngineIntractable
import joshuaradin.gameengine2d.engine.core.basic.GameObject
import joshuaradin.gameengine2d.engine.core.basic.ObjectBehavior
import joshuaradin.gameengine2d.engine.core.scene.Scene
import joshuaradin.gameengine2d.engine.core.scene.SceneManager
import joshuaradin.gameengine2d.engine.core.service.GameObjectTracker

object EditorObjectTracker : EngineIntractable(){

    val editorObjects: MutableList<ObjectBehavior> = mutableListOf()
    val lastLoadedScene: Scene? = null


    private fun findObjects() {
        val newList = mutableListOf<ObjectBehavior>()
        for (activeObject in GameObjectTracker.instance.activeObjects) {
            for (component in activeObject.getAllComponents()) {
                for (annotation in component::class.annotations) {
                    when(annotation) {
                        is RunInEditor -> {
                            newList.add(component)
                        }
                    }
                }
            }
        }


        editorObjects.clear()
        editorObjects.addAll(newList)

    }

    override fun awake() {
        findObjects()
        editorObjects.forEach { it.awake() }
    }

    override fun start() {
        /*
        if(SceneManager.activeScene != lastLoadedScene) findObjects()
        editorObjects.forEach { it.start() }

         */
    }

    override fun update() {
        if(SceneManager.activeScene != lastLoadedScene) findObjects()
        editorObjects.forEach { it.update() }
    }

    override fun fixedUpdate() { }

    override fun onBoundaryEnter(other: GameObject) { }

    override fun onBoundaryStay(other: GameObject) { }

    override fun onBoundaryExit(other: GameObject) { }

    override fun onMouseClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMouseDown() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMouseUp() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMouseStay() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMouseExit() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}