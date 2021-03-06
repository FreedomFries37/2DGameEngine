package joshuaradin.gameengine2d.engine.standard.component

import joshuaradin.gameengine2d.engine.core.basic.GameObject
import joshuaradin.gameengine2d.engine.core.basic.ObjectBehavior
import joshuaradin.gameengine2d.engine.standard.component.rendering.ShapeRenderer
import joshuaradin.gameengine2d.engine.standard.type.Vector2
import joshuaradin.gameengine2d.engine.standard.type.geometry.Point
import joshuaradin.gameengine2d.engine.standard.type.geometry.Square
import joshuaradin.gameengine2d.user.connecting.Screen
import java.awt.Color

open class Camera2D : ObjectBehavior(){


    /**
     * The amount of pixels per unit in the x direction at a (1, 1) scale
     */
    var pixelsPerUnitWidth: Double = 20.0
    /**
     * The amount of pixels per unit in the y direction at a (1, 1) scale
     */
    var pixelsPerUnitHeight: Double = 20.0

    /**
     * The scale of the camera. The closer the value approaches zero, the further zoomed in it would appear
     */
    var scale: Vector2 = Vector2(1, 1)
        set(value) {
            if(value.x < 0 || value.y < 0) throw IllegalArgumentException()
            field = value
        }

    fun zoomIn(mult: Double) {
        if(mult < 0) throw IllegalArgumentException()
        scale.x *= mult
        scale.y *= mult
    }

    fun zoomOut(mult: Double) {
        if(mult < 0) throw IllegalArgumentException()
        zoomIn(1.0 / mult)
    }

    /**
     * Gets the point on the screen where an object would be based on its position and the
     * camera's data
     *
     * @param o the [GameObject] to find its position
     * @param displacement distance away from the object
     * @return the point where the object would be on the screen
     */
    fun getPointOnScreen(o: GameObject, displacement: Point = Point.ZERO) : Point {
        return getPointTransformation(o, displacement)(Point.ZERO)
    }

    fun getPointTransformation(o: GameObject, displacement: Point = Point.ZERO) : (Point) -> Point {
        return {
            val globalPosition = o.getGlobalPosition() + displacement.asVector2() + it.asVector2()
            val cameraPosition = gameObject!!.getGlobalPosition()

            val (xActualDist, yActualDist) = globalPosition - cameraPosition
            val xAngled = transform!!.rotation.cos() * xActualDist + transform!!.rotation.sin() * yActualDist
            val yAngled = transform!!.rotation.sin() * xActualDist + transform!!.rotation.cos() * yActualDist

            val xScaled = xAngled * scale.x
            val yScaled = yAngled * scale.y

            val xPixels = xScaled * pixelsPerUnitWidth
            val yPixels = yScaled * pixelsPerUnitHeight

            val output = Screen.center().asVector2() + Vector2(xPixels, -yPixels)
            output.toPoint()
        }
    }



    override fun start() {
        //addComponent<FrameRateLimiter>()
        val shapeR = addComponent<ShapeRenderer>()
        shapeR?.fillColor = Color.RED
        shapeR?.shape = Square(Point.ZERO)

    }

    override fun toString(): String {
        return "Zoom = $scale"
    }
}