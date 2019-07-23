package joshuaradin.gameengine2d.standard.type

open class Shape protected constructor(val points: List<Point>) {

    companion object {
        private fun join(p1: Point, p2: Point, p3: Point, rest: Array<out Point>): List<Point>{
            val output = mutableListOf(p1, p2, p3)
            output.addAll(rest)
            return output
        }
    }

    constructor(p1: Point, p2: Point, p3: Point, vararg rest: Point) : this(join(p1, p2, p3, rest))

    val lines: List<Line>

    val numPoints: Int get() = points.size
    val area: Double

    init {


        val lines = mutableListOf<Line>()
        for (i in 0 until numPoints - 1) {
            val a = points[i]; val b = points[i+1]
            val l = Line(a, b)
            lines.add(l)
        }
        val lastLine = Line(points.last(), points.first())
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
}