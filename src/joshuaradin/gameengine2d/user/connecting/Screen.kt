package joshuaradin.gameengine2d.user.connecting

import joshuaradin.gameengine2d.standard.component.Camera2D
import joshuaradin.gameengine2d.standard.type.geometry.Point
import joshuaradin.gameengine2d.user.output.GameWindow

class Screen(width: Int, height: Int, frame: GameWindow) {

    companion object {
        var width: Int = 0
            internal set

        var height: Int = 0
            internal set

        var frame: GameWindow? = null
            internal set

        fun center() = Point(width / 2, height / 2)

        fun camera() : Camera2D? = frame?.currentCamera
    }

    init {
        Screen.width = width
        Screen.height = height
        Screen.frame = frame
    }


}