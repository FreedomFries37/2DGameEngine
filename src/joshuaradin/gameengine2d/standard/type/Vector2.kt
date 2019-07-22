package joshuaradin.gameengine2d.standard.type

data class Vector2 (var x: Double, var y: Double) {
    operator fun plus(other: Vector2) : Vector2{
        return Vector2(x + other.x, y + other.y)
    }

    operator fun minus(other: Vector2) : Vector2{
        return Vector2(x - other.x, y - other.y)
    }

    operator fun unaryMinus() : Vector2 {
        return Vector2(-x, -y)
    }
}