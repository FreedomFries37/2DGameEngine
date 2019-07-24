package joshuaradin.gameengine2d.standard.component

import joshuaradin.gameengine2d.standard.type.Line
import joshuaradin.gameengine2d.standard.type.Point
import joshuaradin.gameengine2d.standard.type.Vector2
import joshuaradin.gameengine2d.user.connecting.Time
import joshuaradin.gameengine2d.user.output.I2DRenderer
import joshuaradin.gameengine2d.user.output.Renderer2DComponent
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Paint
import java.awt.image.ImageObserver

class LineRenderer : Renderer2DComponent() {

    var line: Line = Line(Point.ZERO, Point.ZERO)
    var thickness: Float = 1.0f
    var color: Color = Color.BLACK

    override fun render(g: Graphics2D, transform: Transform, camera2D: Camera2D, observer: ImageObserver) {
        g.color = color
        g.stroke = BasicStroke(thickness)
        g.drawLine(line.u.x.toInt(), line.u.y.toInt(), line.v.x.toInt(), line.v.y.toInt())
    }

    override fun update() {
        line.v += Vector2(10.0, 5.0) * Time.deltaTime
    }

}