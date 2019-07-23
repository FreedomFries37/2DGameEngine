package joshuaradin.gameengine2d.standard.type

import java.lang.Math.*

class Line (var u: Point, var v: Point) {
    val length: Double
        get() = u.distance(v)

    val angle: Double
        get() = atan((v.y - u.y) / (v.x - u.x))


    operator fun times(n: Double) : Line{
        val newLength = length*n
        val newXDiff = newLength * cos(angle)
        val newYDiff = newLength * sin(angle)

        val newV = Point(u.x + newXDiff, u.y + newYDiff)
        return Line(u, newV)
    }

}
