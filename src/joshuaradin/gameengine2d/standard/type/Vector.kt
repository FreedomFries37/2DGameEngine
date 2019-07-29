package joshuaradin.gameengine2d.standard.type

import joshuaradin.gameengine2d.standard.type.geometry.Rotation
import java.io.Serializable


open class Vector(val displacement: Double, val angle: Rotation = Rotation()) : Serializable {

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
        return this + other.displacement + other.angle
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


    operator fun component1() : Double {
        return asVector2().x
    }

    operator fun component2() : Double {
        return asVector2().y
    }

}

@Deprecated("Unneeded", replaceWith = ReplaceWith("Nothing"))
fun Vector2.asVector() : Vector {
    return Vector(displacement(), angle())
}