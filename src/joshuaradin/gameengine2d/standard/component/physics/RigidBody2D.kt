package joshuaradin.gameengine2d.standard.component.physics

import joshuaradin.gameengine2d.standard.component.InterferenceBoundary
import joshuaradin.gameengine2d.standard.type.Vector
import joshuaradin.gameengine2d.standard.type.geometry.Rotation
import joshuaradin.gameengine2d.user.connecting.Time

class RigidBody2D : InterferenceBoundary() {

    var mass: Double = 1.0
    var useGravity: Boolean = false

    private var acceleration: Vector = Vector(0.0)

    private var velocity: Vector = Vector(0.0)
    private var angularAcceleration: Rotation = Rotation()



    override fun fixedUpdate() {
        velocity += acceleration * Time.fixedDeltaTime

        transform!!.rotation += angularAcceleration
        transform!!.position += velocity.asVector2()
    }

    fun applyForce(force: Vector) {

    }
}