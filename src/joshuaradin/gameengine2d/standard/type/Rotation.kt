package joshuaradin.gameengine2d.standard.type

import kotlin.math.PI

data class Rotation(var radians: Double) {
    companion object {
        fun createDeg(deg: Double) = Rotation(deg * PI / 180.0)
    }

    operator fun plus(other: Rotation) : Rotation{
        return Rotation(radians + other.radians)
    }

    operator fun minus(other: Rotation) : Rotation{
        return Rotation(radians - other.radians)
    }
}