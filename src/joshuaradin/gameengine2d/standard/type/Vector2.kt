package joshuaradin.gameengine2d.standard.type

import kotlin.math.nextDown

data class Vector2 (var x: Double, var y: Double) {


    constructor(other: Vector2) : this(other.x, other.y)

    operator fun plus(other: Vector2) : Vector2{
        return Vector2(x + other.x, y + other.y)
    }

    operator fun plus(other: Int) : Vector2{
        return Vector2(x + other, y + other)
    }

    operator fun minus(other: Vector2) : Vector2{
        return Vector2(x - other.x, y - other.y)
    }

    operator fun unaryMinus() : Vector2 {
        return Vector2(-x, -y)
    }

    operator fun times(other: Vector2) : Vector2{
        return Vector2(x * other.x, y * other.y)
    }

    fun toPoint() : Point {
        return Point(x, y)
    }

    fun round() : Vector2 {
        val vector2 = Vector2(this)
        vector2.x = x % .00001
        vector2.y = y % .00001
        return vector2
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vector2) return false

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    override fun toString(): String {
        return "(x=$x, y=$y)"
    }


}