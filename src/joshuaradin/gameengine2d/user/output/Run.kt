package joshuaradin.gameengine2d.user.output

import joshuaradin.gameengine2d.core.GameObjectTracker
import joshuaradin.gameengine2d.core.ProjectInfo
import joshuaradin.gameengine2d.core.scene.Scene
import joshuaradin.gameengine2d.core.scene.SceneManager

fun main() {
    run(ProjectInfo("Test"))
}


fun run(info: ProjectInfo){

    val goTracker = SceneManager.initGameObjectTracker()
    val gameWindow = GameWindow(info.name)


    goTracker.sceneChange(SceneManager.activeScene!!)

    while (gameWindow.isShowing){
        val startFrameTime = System.currentTimeMillis()
        goTracker.update()
        val endFrameTime = System.currentTimeMillis()



    }

}