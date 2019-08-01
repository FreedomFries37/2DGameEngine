package joshuaradin.gameengine2d.user.output

import joshuaradin.gameengine2d.core.ProjectInfo
import joshuaradin.gameengine2d.core.scene.SceneManager
import joshuaradin.gameengine2d.standard.component.Camera2D
import joshuaradin.gameengine2d.test.PhysicsScript
import joshuaradin.gameengine2d.test.TestScript
import joshuaradin.gameengine2d.user.connecting.Time
import kotlin.math.pow

fun main() {
    val info = ProjectInfo("Test")

    val baseScene = SceneManager.addScene()!!
    val gO1 = baseScene.createGameObject()
    val gO2 = baseScene.createGameObject()

    /*
    gO2.setParent(gO1)
    gO2.transform.position += Vector2(10.0, 0.0)
    gO2.transform.rotation = Rotation.createDeg(45.0)



    gO1.transform.rotation += Rotation.createDeg(-90.0)
    GameObjectTracker.instance.printAllObjectSceneAndPosition()

    gO1.transform.position += Vector2(15.0, 0.0)
    GameObjectTracker.instance.printAllObjectSceneAndPosition()

    gO1.transform.rotation += Rotation.createDeg(45.0)
    GameObjectTracker.instance.printAllObjectSceneAndPosition()
    */


    gO1.addComponent<TestScript>()
    gO2.addComponent<PhysicsScript>()

    info.scenes.add(baseScene)

    Run.run(info)
}

object Run {
    var runningWindow: GameWindow? = null
        private set

    internal fun run(info: ProjectInfo) {

        val goTracker = SceneManager.initGameObjectTracker()
        runningWindow = GameWindow(info.name, info.windowSize, goTracker)


        goTracker.sceneChange(info.initialScene)
        runningWindow?.currentCamera = info.initialScene.getGameObjectWithComponent<Camera2D>()?.getComponent()

        var timer = 0.0

        while (runningWindow?.isShowing!!) {

            val startScene = goTracker.currentScene
            val startFrameTime = System.nanoTime()

            goTracker.update()
            runningWindow?.repaint()

            val endFrameTime = System.nanoTime()
            val d = (endFrameTime - startFrameTime) / 10.0.pow(9)

            Time.deltaTime = d
            timer += Time.deltaTime
            if(timer >= Time.fixedDeltaTime) {
                timer = 0.0
                goTracker.fixedUpdate()
            }

            val fps = 1.0 / d


            if(goTracker.currentScene != startScene) {
                runningWindow?.currentCamera = goTracker.currentScene?.getGameObjectWithComponent<Camera2D>()?.getComponent()
            }
        }

        runningWindow = null
    }


}