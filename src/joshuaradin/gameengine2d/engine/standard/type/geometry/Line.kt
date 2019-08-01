package joshuaradin.gameengine2d.engine.standard.type.geometry

import joshuaradin.gameengine2d.engine.standard.type.PositionAdjustable
import joshuaradin.gameengine2d.engine.standard.type.Vector2
import java.lang.Math.*
import kotlin.math.absoluteValue

/**
 * Represents a line between two points
 *
 * @property u the start of the line
 * @property v the end of the line
 */
data class Line (var u: Point = Point.ZERO, var v: Point = Point.ZERO) :
    PositionAdjustable<Line> {
    val length: Double
        get() = u.distance(v).absoluteValue

    val angle: Double
        get() {
            val d = u.x - v.x
            if(d == 0.0) return if(u.y > v.y) {
                Rotation.createDeg(90.0).radians
            } else if( u.y < v.y) {
                Rotation.createDeg(-90.0).radians
            } else {
                Rotation.createDeg(0.0).radians
            }
            return kotlin.math.atan((u.y - v.y) / d) + if (u.x < v.x) kotlin.math.PI else 0.0
        }

    val asFunction get() = toFunction()

    val center: Point
        get() {
            var output = u + v
            output = Point(output.x / 2, output.y / 2)
            return output
        }

    val slope: Double get() {
        return (v.y - u.y) / (v.x - u.x)
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


    /**
     * Rotates around [u]
     */
    override fun rotate(r: Rotation): Line {
        val difference = v - u.asVector2()
        val change = difference.asVector2().rotate(r)
        return Line(u.copy(), u.copy() + change)
    }

    override infix fun recenter(newCenter: Vector2): Line {
        val cAsV2 = center.asVector2()
        val vDiffOriginal = v - cAsV2
        val uDiffOriginal = u - cAsV2

        return copy(u = uDiffOriginal + newCenter, v = vDiffOriginal + newCenter)
    }

    infix fun relativeTo(position: Point) : Line {
        return copy(u = u + position, v = v + position)
    }

    fun toFunction() : (x: Double) -> Double = { x ->
        val m = (v.y - u.y) / (v.x - u.x)
        val b = u.y
        m*x + b
    }

    fun toVector2() : Vector2 {
        return Vector2(v.x - u.x, v.y - u.y)
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

    /*
    fun angleWith(other: Line) : Rotation? {
        if(other.u != v) return null


        val m1 = slope
        val m2 = other.slope

        if(m1.absoluteValue == Double.POSITIVE_INFINITY || m2.absoluteValue == Double.POSITIVE_INFINITY) {
            val Y1 = max(u.y, v.y)
            val Y2 = min(u.y, v.y)
            val numerator = Y1 - Y2
            val denominator = ((u.x - v.x).pow(2) + (Y1 - Y2).pow(2)).pow(.5)
            return Rotation(acos(numerator / denominator))

        }


        return Rotation(atan2(m1 -m2, 1 + m1*m2))
    }

     */

    fun angleWith(other: Line) : Rotation? {
        if(u != other.u) return null


        val thisV2 = toVector2()
        val otherV2 = other.toVector2()

        val dotproduct = thisV2 dot otherV2
        val length = thisV2.displacement() * otherV2.displacement()
        val angle = acos(dotproduct / length)

        return Rotation(angle)
    }

    override fun transform(function: (Point) -> Point): Line {
        return copy(u = function(u.copy()), v = function(v.copy()))
    }
}

fun main() {



    var a = Line(Point.ZERO, Point(0, 5))
    var b = Line(Point.ZERO, Point(5, 0))
    println(a.angleWith(b))
    println(b.angleWith(a))

    a = Line(Point.ZERO, Point(4, 0))
    b = Line(Point.ZERO, Point(5, 3))
    println(a.angleWith(b))
}
