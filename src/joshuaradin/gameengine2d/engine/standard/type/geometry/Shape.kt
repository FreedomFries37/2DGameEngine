package joshuaradin.gameengine2d.engine.standard.type.geometry

import joshuaradin.gameengine2d.engine.standard.type.PositionAdjustable
import joshuaradin.gameengine2d.engine.standard.type.Vector2
import kotlin.math.PI
import kotlin.math.atan


open class Shape protected constructor(points: List<Point>) :
    PositionAdjustable<Shape>, IBoundary {

    companion object {
        private fun join(p1: Point, p2: Point, p3: Point, rest: Array<out Point>): List<Point>{
            val output = mutableListOf(p1, p2, p3)
            output.addAll(rest)
            return output
        }


    }

    constructor(p1: Point, p2: Point, p3: Point, vararg rest: Point) : this(
        join(p1, p2, p3, rest)
    )

    var points: List<Point>
        protected set

    val lines: List<Line>

    val numPoints: Int get() = points.size
    val area: Double
    val center: Point
        get() {
            var output = Point.ZERO
            for (point in points) {
                output += point
            }
            output = Point(output.x / numPoints, output.y / numPoints)
            return output
        }

    init {
        val (x, y) = Point.centerOf(*points.toTypedArray())
        this.points = points.sortedBy { (atan((it.y - y) / (it.x - x)) + (if ((it.x - x) < 0) PI else 0.0)) }

        val lines = mutableListOf<Line>()
        for (i in points.size - 1 downTo 1) {
            val a = points[i]; val b = points[i-1]
            val l = Line(a, b)
            lines.add(l)
        }
        val lastLine = Line(points.first(), points.last())
        lines.add(lastLine)
        this.lines = lines

        area = calculateArea()
    }

    private fun getPoint(n: Int) : Point {
        return points[n % numPoints]
    }

    private fun calculateArea() : Double {
        var sum = 0.0
        for (i in 0 until numPoints) {
            val pI = getPoint(i)
            val pIP1 = getPoint(i + 1)
            sum += (pI.x * pIP1.y - pIP1.x * pI.y)
        }
        sum /= 2.0
        return sum
    }

    override fun toString(): String {
        return "${this::class.simpleName}($numPoints) area = $area"
    }

    fun printInfo() {
        println(this)
        for ((index: Int, point: Point) in points.withIndex()) {
            println("p$index = $point")
        }
        println()
    }

    override infix fun moveAccordingTo(v: Vector2) : Shape {
        if(v == Vector2(
                0,
                0
            )
        ) return Shape(points)
        val list = List(numPoints) { getPoint(it).copy() }
        for (i in 0 until list.size){
            val point = list[i]

            point.x += v.x
            point.y += v.y
        }
        return Shape(list)
    }

    override infix fun rotate(r: Rotation): Shape {

        if(r == Rotation()) return Shape(points)

        val list = List(numPoints) { Point.ZERO.copy() }

        for (i in 0 until list.size){
            var line = Line(center, getPoint(i))
            line = line rotate r

            list[i].x = line.v.x
            list[i].y = line.v.y
        }

        return Shape(list)
    }

    override fun recenter(newCenter: Vector2): Shape {
        return moveAccordingTo((newCenter - center.asVector2()))
    }

    override infix fun scaleTo(v: Vector2) : Shape {
        if(v == Vector2(
                1,
                1
            )
        ) return Shape(points)
        val list = List(numPoints) { getPoint(it).copy() }
        val center = center
        for (i in 0 until list.size){
            val point = list[i]

            point.x = (point.x - center.x) * v.x + center.x
            point.y = (point.y - center.y) * v.y + center.y
        }
        return Shape(list)
    }

    override infix fun transform(function: (Point) -> Point) : Shape {
        val list = MutableList(numPoints) { getPoint(it).copy() }
        for (i in 0 until list.size){
            list[i] = function(list[i])
        }
        return Shape(list)
    }

    override fun inBounds(point: Point) : Boolean {
        return !lines.any { it.pointAbove(point) }
    }

    override fun inBounds(shape: Shape) : Boolean {
        val set = HashSet(shape.points)
        for (line in lines) {
            set.removeIf { line.pointAbove(it) }
        }

        return set.isEmpty().not()
    }
}