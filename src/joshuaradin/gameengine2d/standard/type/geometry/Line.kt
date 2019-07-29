package joshuaradin.gameengine2d.standard.type.geometry

import joshuaradin.gameengine2d.standard.type.PositionAdjustable
import joshuaradin.gameengine2d.standard.type.Vector2
import java.lang.Math.*

data class Line (var u: Point = Point.ZERO, var v: Point = Point.ZERO) :
    PositionAdjustable<Line> {
    val length: Double
        get() = u.distance(v)

    val angle: Double
        get() = atan((v.y - u.y) / (v.x - u.x))

    val asFunction get() = toFunction()

    val center: Point
        get() {
            var output = u + v
            output = Point(output.x / 2, output.y / 2)
            return output
        }


    override operator fun times(n: Double) : Line {
        val newLength = length*n
        val newXDiff = newLength * cos(angle)
        val newYDiff = newLength * sin(angle)

        val newV = Point(u.x + newXDiff, u.y + newYDiff)
        return Line(u, newV)
    }

    override fun moveAccordingTo(v: Vector2): Line {
        return copy(u = this.u + v, v = this.v + v)
    }

    override fun scaleTo(v: Vector2): Line {
        return times(v.displacement())
    }

    override fun rotate(r: Rotation): Line {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun recenter(newCenter: Vector2): Line {
        val cAsV2 = center.asVector2()
        val vDiffOriginal = v - cAsV2
        val uDiffOriginal = u - cAsV2

        return copy(u = uDiffOriginal + newCenter, v = vDiffOriginal + newCenter)
    }

    fun toFunction() : (x: Double) -> Double = { x ->
        val m = (v.y - u.y) / (v.x - u.x)
        val b = u.y
        m*x + b
    }

    fun pointAbove(point: Point) : Boolean {
        if(u.x == v.x) return if (u.y < v.y) {
            point.x < u.x
        } else {
            point.x >= u.x
        }
        return if(isForward()) {
            point.y > asFunction(point.x)
        } else {
            point.y < asFunction(point.x)
        }
    }

    fun isForward() = v.x > u.x

    fun pointBelow(point: Point) : Boolean = !pointAbove(point)
    override fun toString(): String {
        return "Line(u=$u, v=$v)"
    }


}
