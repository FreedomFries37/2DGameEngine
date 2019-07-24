package joshuaradin.gameengine2d.standard.type

import kotlin.math.pow


data class Point(var x: Double, var y: Double) : PositionAdjustable<Point>{
    companion object {
        val ZERO = Point(0.0, 0.0)

        fun centerOf(vararg points: Point) : Point{
            var output = ZERO
            for (point in points) {
                output += point
            }
            return output.copy(x = output.x/points.size, y = output.y/points.size)
        }



        fun recenter(vararg points: Point) : Array<Point> {
            val center = centerOf(*points).asVector2()
            val output = Array(points.size) { points[it].copy()}
            for ((index, point) in output.withIndex()) {
                output[index] = point - center
            }
            return output
        }
    }

    constructor(x: Int, y: Int) : this(x.toDouble(), y.toDouble())
    constructor(other: Point) : this(other.x, other.y)

    override fun toString(): String {
        return "Point(x=$x, y=$y)"
    }

    fun distance(other: Point) : Double{
        return ((x - other.x).pow(2) + (y - other.y).pow(2)).pow(0.5)
    }

    fun displacement() = distance(ZERO)

    fun angle(other: Point) = Line(this, other).angle

    override operator fun plus(o: Vector2) : Point{
        val output = copy()
        output.x += o.x
        output.y += o.y
        return output
    }
    operator fun plus(o: Point) : Point{
        val output = copy()
        output.x += o.x
        output.y += o.y
        return output
    }

    override operator fun minus(o: Vector2) : Point{
        return this + -o
    }

    fun asVector2() : Vector2 {
        return Vector2(x, y)
    }

    override fun moveAccordingTo(v: Vector2): Point {
        return plus(v)
    }

    override fun scaleTo(v: Vector2): Point {
        return copy()
    }

    override fun rotate(r: Rotation): Point {
        return copy()
    }

    override fun recenter(newCenter: Vector2): Point {
        return copy()
    }
}