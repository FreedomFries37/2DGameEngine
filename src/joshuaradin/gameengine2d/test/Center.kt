package joshuaradin.gameengine2d.test

import joshuaradin.gameengine2d.core.basic.GameObject
import joshuaradin.gameengine2d.standard.component.LineRenderer
import joshuaradin.gameengine2d.standard.type.Line
import joshuaradin.gameengine2d.standard.type.Point

object Center {
    val gameObject = GameObject.createEmpty(null)

    init {
        gameObject.name = "Center"
        val xLine: LineRenderer? = gameObject.addComponent()
        xLine?.line = Line(Point(-1000, 0), Point(1000, 0))

        val yLine: LineRenderer? = gameObject.addComponent()
        yLine?.line = Line(Point(0, -1000), Point(0, 1000))
    }
}