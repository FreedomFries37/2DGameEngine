package joshuaradin.gameengine2d.standard.type

import kotlin.math.pow

class Point(var x: Double, var y: Double) {
    companion object {
        val ZERO = Point(0.0, 0.0)
    }

    constructor(x: Int, y: Int) : this(x.toDouble(), y.toDouble())

    override fun toString(): String {
        return "Point(x=$x, y=$y)"
    }

    fun distance(other: Point) : Double{
        return ((x - other.x).pow(2) + (y - other.y).pow(2)).pow(0.5)
    }
}