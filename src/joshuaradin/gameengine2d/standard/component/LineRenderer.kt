package joshuaradin.gameengine2d.standard.component

import joshuaradin.gameengine2d.standard.type.Line
import joshuaradin.gameengine2d.standard.type.Point
import joshuaradin.gameengine2d.user.output.Renderer2DComponent
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.ImageObserver

class LineRenderer : Renderer2DComponent() {

    var line: Line = Line(Point.ZERO, Point.ZERO)
    var thickness: Float = 1.0f
    var color: Color = Color.BLACK


    override fun render(g: Graphics2D, transform: Transform, camera2D: Camera2D, observer: ImageObserver) {
        g.color = color
        g.stroke = BasicStroke(thickness)

        val distanceFromCamera = getDistanceFromCamera(transform, camera2D)
        var lineAdj = line.copy()
        lineAdj += distanceFromCamera

        g.drawLine(lineAdj.u.x.toInt(), lineAdj.u.y.toInt(), lineAdj.v.x.toInt(), lineAdj.v.y.toInt())
    }

    override fun toString(): String {
        return line.toString()

    }


}