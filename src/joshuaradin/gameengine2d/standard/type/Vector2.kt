package joshuaradin.gameengine2d.standard.type

import java.io.Serializable
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin


data class Vector2 (var x: Double, var y: Double) : Serializable{

    companion object {

        fun distanceWithRot(dist: Double, r: Rotation) : Vector2 =
            distanceWithRot(dist, r.radians)

        fun distanceWithRot(dist: Double, rot: Double) : Vector2 {
            return Vector2(dist*cos(rot), dist*sin(rot))
        }
    }

    constructor(other: Vector2) : this(other.x, other.y)
    constructor(x: Int, y: Int) : this(x.toDouble(), y.toDouble())



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

    operator fun times(other: Double) : Vector2{
        return Vector2(x * other, y * other)
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

    fun displacement() : Double {
        return (x.pow(2) + y.pow(2)).pow(0.5)
    }


}