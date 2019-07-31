package joshuaradin.gameengine2d.standard.component

import joshuaradin.gameengine2d.core.basic.GameObject
import joshuaradin.gameengine2d.core.basic.ObjectBehavior
import joshuaradin.gameengine2d.standard.component.rendering.ShapeRenderer
import joshuaradin.gameengine2d.standard.type.Vector2
import joshuaradin.gameengine2d.standard.type.geometry.Point
import joshuaradin.gameengine2d.standard.type.geometry.Square
import joshuaradin.gameengine2d.user.connecting.Screen
import java.awt.Color

class Camera2D : ObjectBehavior(){


    /**
     * The amount of pixels per unit in the x direction at a (1, 1) scale
     */
    var unitsPerPixelWidth: Double = 1.0 / 1
    /**
     * The amount of pixels per unit in the y direction at a (1, 1) scale
     */
    var unitsPerPixelHieght: Double = 1.0 / 1

    /**
     * The scale of the camera. The closer the value approaches zero, the further zoomed in it would appear
     */
    var scale: Vector2 = Vector2(1, 1)
        set(value) {
            if(value.x < 0 || value.y < 0) throw IllegalArgumentException()
            field = value
        }

    fun zoomOut(mult: Double) {
        if(mult < 0) throw IllegalArgumentException()
        scale.x *= mult
        scale.y *= mult
    }

    fun zoomIn(mult: Double) {
        if(mult < 0) throw IllegalArgumentException()
        zoomOut(1.0 / mult)
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
        val globalPosition = o.getGlobalPosition() + displacement.asVector2()
        val cameraPosition = gameObject!!.getGlobalPosition()

        val (xActualDist, yActualDist) = globalPosition - cameraPosition
        val xAngled = transform!!.rotation.cos() * xActualDist + transform!!.rotation.sin() * yActualDist
        val yAngled = transform!!.rotation.sin() * xActualDist + transform!!.rotation.cos() * yActualDist

        val xScaled = xAngled * scale.x
        val yScaled = yAngled * scale.y

        val xPixels = xScaled * unitsPerPixelWidth
        val yPixels = yScaled * unitsPerPixelHieght

        val output = Screen.center().asVector2() + Vector2(xPixels, -yPixels)
        return output.toPoint()
    }




    override fun start() {
        //addComponent<FrameRateLimiter>()
        val shapeR = addComponent<ShapeRenderer>()
        shapeR?.borderColor = Color.RED
        shapeR?.shape = Square(Point.ZERO)

    }

    override fun toString(): String {
        return "Zoom = $scale"
    }
}