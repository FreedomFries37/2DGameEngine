package joshuaradin.gameengine2d.standard.type.geometry

import java.io.Serializable
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Rotation(radians: Double = 0.0) : Serializable{


    private var _radians: Double = radians % (2 * PI)
    var radians: Double
        get() = _radians
        set(value) {
            _radians = value % (2 * PI)
        }
    var degrees: Double
        get() = _radians.toDegrees()
        set(value) {
            radians = value.toRadians()
        }

    constructor(other: Rotation) : this(other.radians)

    companion object {
        fun createDeg(deg: Double) = Rotation(deg * PI / 180.0)

        val UP = createDeg(90.0)
        val DOWN = createDeg(-90.0)
        val FORWARD = createDeg(0.0)
        val BACKWARD = createDeg(180.0)

        val CLOCKWISE = UP
        val COUNTERCLOCKWISE = DOWN
    }

    operator fun plus(other: Rotation) : Rotation {
        return Rotation(radians + other.radians)
    }

    operator fun minus(other: Rotation) : Rotation {
        return Rotation(radians - other.radians)
    }

    operator fun times(other: Double) : Rotation {
        return Rotation(radians * other)
    }

    operator fun div(other: Double) : Rotation {
        return Rotation(radians / other)
    }

    operator fun inc() : Rotation {
        return Rotation((radians.toDegrees() + 1).toRadians())
    }



    /**
     * Converts degrees to radians
     */
    fun Double.toRadians() : Double {
        return (this / 180) * PI
    }

    /**
     *  Converts radians to degrees
     */
    fun Double.toDegrees() : Double{
        return (this / PI) * 180
    }

    fun round() : Rotation {
        val output = Rotation(this)
        output.radians = radians % .000001
        return output
    }

    fun sin() : Double {
        return sin(radians)
    }

    fun cos() : Double {
        return cos(radians)
    }

    fun tan() : Double {
        return tan(radians)
    }

    override fun toString(): String {
        return "$degrees*"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Rotation) return false

        if (_radians != other._radians) return false

        return true
    }

    override fun hashCode(): Int {
        return _radians.hashCode()
    }
}