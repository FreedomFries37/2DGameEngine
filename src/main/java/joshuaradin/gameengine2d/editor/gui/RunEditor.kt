package joshuaradin.gameengine2d.editor.gui

import joshuaradin.gameengine2d.editor.services.EditorObjectTracker
import joshuaradin.gameengine2d.engine.core.ProjectInfo
import joshuaradin.gameengine2d.engine.core.scene.SceneManager
import joshuaradin.gameengine2d.user.connecting.Time
import joshuaradin.gameengine2d.user.output.projectInfoDemo
import kotlin.math.pow


fun main(args: Array<String>) {
    val projectInfo = projectInfoDemo()
    EditorRun.run(projectInfo)


}


object EditorRun {
    var runningWindow: EditorWindow? = null
        private set

    internal fun run(info: ProjectInfo) {


        runningWindow = EditorWindow(info)



        EditorObjectTracker.awake()
        EditorObjectTracker.start()


        while (runningWindow?.isShowing!!) {

            val startScene = SceneManager.activeScene
            val startFrameTime = System.nanoTime()

            EditorObjectTracker.update()
            runningWindow?.repaint()

            val endFrameTime = System.nanoTime()
            val d = (endFrameTime - startFrameTime) / 10.0.pow(9)

            Time.deltaTime = d


            val fps = 1.0 / d


            if(SceneManager.activeScene != startScene) {
                //runningWindow?.currentCamera = runningWindow!!.editorCameraMap[SceneManager.activeScene]
                EditorObjectTracker.start()
            }
        }

        runningWindow = null
    }


}