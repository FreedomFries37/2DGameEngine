package joshuaradin.gameengine2d.standard.component

import joshuaradin.gameengine2d.core.basic.ObjectBehavior
import joshuaradin.gameengine2d.standard.type.Point
import joshuaradin.gameengine2d.standard.type.Square
import java.awt.Color

class Camera2D : ObjectBehavior(){

    override fun start() {
        addComponent<FrameRateLimiter>()
        val shapeR = addComponent<ShapeRenderer>()
        shapeR?.borderColor = Color.RED
        shapeR?.shape = Square(Point.ZERO)

    }
}