package joshuaradin.gameengine2d.standard.component.rendering


import joshuaradin.gameengine2d.standard.component.Camera2D
import joshuaradin.gameengine2d.standard.component.Transform
import joshuaradin.gameengine2d.standard.type.geometry.Shape
import joshuaradin.gameengine2d.user.output.Renderer2DComponent
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Polygon
import java.awt.image.ImageObserver
import kotlin.math.roundToInt

class ShapeRenderer : Renderer2DComponent() {



    var shape: Shape? = null

    var fillColor: Color = Color.WHITE
    var borderWidth: Float = 10f
    var borderColor: Color = Color.GRAY

    override fun render(g: Graphics2D, transform: Transform, camera2D: Camera2D, observer: ImageObserver) {
        if(shape != null) {
            g.stroke = BasicStroke(borderWidth*transform.scale.displacement().toFloat())




            val pointOnScreen = camera2D.getPointOnScreen(transform.gameObject!!)


            var adjustedShape = shape!!
            adjustedShape *= transform.scale

            val distanceFromCamera = getDistanceFromCamera(transform, camera2D)
            adjustedShape += pointOnScreen.asVector2()

            val rotDiff = transform.rotation - camera2D.transform!!.rotation
            adjustedShape = adjustedShape rotate rotDiff


            val shapeAsPolygon = adjustedShape.toPolygon()
            g.color = borderColor
            g.draw(shapeAsPolygon)

            g.color = fillColor
            g.fill(shapeAsPolygon)
        }
    }

    fun Shape.toPolygon() : Polygon {
        val xPoints = Array(numPoints) {0}
        val yPoints = Array(numPoints) {0}


        for ((index, point) in points.withIndex()) {
            xPoints[index] = point.x.roundToInt()
            yPoints[index] = point.y.roundToInt()
        }

        return Polygon(xPoints.toIntArray(), yPoints.toIntArray(), numPoints)
    }

    override fun toString(): String {
        return "$shape at ${shape?.center}"
    }
}
