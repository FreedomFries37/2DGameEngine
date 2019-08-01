package joshuaradin.gameengine2d.standard.type

import joshuaradin.gameengine2d.standard.type.geometry.Rotation
import java.io.Serializable


open class Vector(val displacement: Double, val angle: Rotation = Rotation()) : Serializable {

    constructor(vector2: Vector2) : this(vector2.displacement(), vector2.angle())

    fun asVector2() : Vector2 {
        return Vector2.distanceWithRot(displacement, angle)
    }

    operator fun plus(displacement: Double) : Vector {
        return Vector(this.displacement + displacement, angle)
    }

    operator fun plus(angle: Rotation) : Vector {
        return Vector(this.displacement, this.angle + angle)
    }

    operator fun plus(other: Vector) : Vector {
        return Vector(this.asVector2() + other.asVector2())
    }


    operator fun minus(displacement: Double) : Vector {
        return Vector(this.displacement - displacement, angle)
    }

    operator fun minus(angle: Rotation) : Vector {
        return Vector(this.displacement, this.angle - angle)
    }

    operator fun minus(other: Vector) : Vector {
        return this - other.displacement - other.angle
    }

    operator fun times(other: Double) : Vector {
        return Vector(displacement * other, angle)
    }

    operator fun div(other: Double) : Vector {
        return Vector(displacement / other, angle)
    }


    operator fun component1() : Double {
        return asVector2().x
    }

    operator fun component2() : Double {
        return asVector2().y
    }

    override fun toString(): String {
        return asVector2().toString()
    }
}

@Deprecated("Unneeded", replaceWith = ReplaceWith("Nothing"))
fun Vector2.asVector() : Vector {
    return Vector(displacement(), angle())
}