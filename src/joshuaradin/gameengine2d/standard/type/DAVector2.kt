package joshuaradin.gameengine2d.standard.type

data class DAVector2(val displacement: Double, val angle: Rotation = Rotation())  {

    fun asVector2() : Vector2 {
        return Vector2.distanceWithRot(displacement, angle)
    }

    operator fun plus(displacement: Double) : DAVector2 {
        return DAVector2(this.displacement + displacement, angle)
    }

    operator fun plus(angle: Rotation) : DAVector2 {
        return DAVector2(this.displacement, this.angle + angle)
    }

    operator fun plus(other: DAVector2) : DAVector2 {
        return this + other.displacement + other.angle
    }

    operator fun plus(other: Vector2) : DAVector2 {
        return this + other.asDistanceAngle()
    }

    operator fun minus(displacement: Double) : DAVector2 {
        return DAVector2(this.displacement - displacement, angle)
    }

    operator fun minus(angle: Rotation) : DAVector2 {
        return DAVector2(this.displacement, this.angle - angle)
    }

    operator fun minus(other: DAVector2) : DAVector2 {
        return this - other.displacement - other.angle
    }

    operator fun minus(other: Vector2) : DAVector2 {
        return this - other.asDistanceAngle()
    }
}


fun Vector2.asDistanceAngle() : DAVector2 {
    return DAVector2(displacement(), angle())
}