package joshuaradin.gameengine2d.editor.gui


import joshuaradin.gameengine2d.engine.core.ProjectInfo
import joshuaradin.gameengine2d.engine.core.scene.Scene
import joshuaradin.gameengine2d.engine.core.scene.SceneManager
import joshuaradin.gameengine2d.engine.core.service.GameObjectTracker
import joshuaradin.gameengine2d.engine.standard.component.Camera2D
import joshuaradin.gameengine2d.user.output.RenderPanel
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JFrame

class EditorWindow(val info: ProjectInfo) : JFrame("Editor") {

    val editorCameraMap = mutableMapOf<Scene, EditorCamera>()

    var mainPanel: RenderPanel

    var gameObjectTracker: GameObjectTracker
        get() = mainPanel.gameObjectTracker!!
        set(value) {
            mainPanel.gameObjectTracker = value
        }
    var currentCamera: Camera2D?
        get() = mainPanel.currentCamera
        set(value) {
            mainPanel.currentCamera = value
        }

    init {
        preferredSize = Dimension(1200, 800)
        defaultCloseOperation = EXIT_ON_CLOSE
        contentPane.layout = BorderLayout()

        val renderPanel = RenderPanel()
        add(renderPanel, BorderLayout.CENTER)
        mainPanel = renderPanel

        val objectTracker = GameObjectTracker.initialize(info.toSceneGameObjectPairs())

        gameObjectTracker = objectTracker

        loadScene(info.initialScene)

        pack()
        isVisible = true


    }




    fun loadScene(num: Int) {
        loadScene(info.scenes[num])
    }

    fun loadScene(scene: Scene) {
        if(!editorCameraMap.containsKey(scene)) {
            val camera = scene.createGameObject {
                val gameObject = scene.createGameObject()
                gameObject.name = "EditorCamera"
                gameObject.addComponent<EditorCamera>()
                gameObject.editorOnly = true
                gameObject
            }
            editorCameraMap[scene] = camera.getComponent<EditorCamera>()!!
        }

        if(gameObjectTracker.awareOfScene(scene)) GameObjectTracker.instance.addScene(scene)
        SceneManager.sceneChange(scene)
        currentCamera = editorCameraMap[scene]
    }

}