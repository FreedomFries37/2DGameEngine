package joshuaradin.gameengine2d.standard.component

import joshuaradin.gameengine2d.user.output.Renderer2DComponent
import java.awt.Graphics2D
import java.awt.Polygon
import joshuaradin.gameengine2d.standard.type.Shape as Shape
import java.awt.image.ImageObserver
import kotlin.math.roundToInt

class ShapeRenderer : Renderer2DComponent() {

    private var shapeAsPolygon: Polygon? = null
    private var _shape: Shape? = null
    var shape: Shape?
        get() = _shape
        set(value) {
            _shape = value
            shapeAsPolygon = value?.toPolygon()
        }


    override fun render(g: Graphics2D, transform: Transform, camera2D: Camera2D, observer: ImageObserver) {
        if(shape != null) {
            g.draw(shapeAsPolygon)
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
}