package joshuaradin.gameengine2d.engine.standard.type

import joshuaradin.gameengine2d.engine.standard.type.geometry.Point
import joshuaradin.gameengine2d.engine.standard.type.geometry.Rotation
import java.io.Serializable

interface PositionAdjustable<T> : Serializable{

    infix fun moveAccordingTo(v: Vector2) : T
    infix fun scaleTo(v: Vector2) : T
    infix fun rotate(r: Rotation) : T
    infix fun recenter(newCenter: Vector2): T
    infix fun transform(function: (Point) -> (Point)) : T

    operator fun plus (v: Vector2) : T {
        return this moveAccordingTo v
    }

    operator fun minus (v: Vector2) : T {
        return this moveAccordingTo -v
    }

    operator fun times(v: Vector2) : T {
        return this scaleTo v
    }

    operator fun div(v: Vector2) : T {
        return this * Vector2(1 / v.x, 1 / v.y)
    }

    infix fun scaleTo(d: Double) : T {
        return this scaleTo Vector2.distanceWithRot(
            d,
            Rotation.createDeg(45.0)
        )
    }

    operator fun times(d: Double) : T {
        return this scaleTo d
    }

    operator fun div(d: Double) : T {
        return this * (1.0 / d)
    }
}