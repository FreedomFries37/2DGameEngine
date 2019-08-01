package joshuaradin.gameengine2d.standard.component.physics

import joshuaradin.gameengine2d.standard.component.InterferenceBoundary
import joshuaradin.gameengine2d.standard.type.Vector
import joshuaradin.gameengine2d.standard.type.geometry.Point
import joshuaradin.gameengine2d.user.connecting.Time


class RigidBody2D : InterferenceBoundary() {

    var mass: Double = 1.0
    var useGravity: Boolean = false



    override fun awake() {

    }

    override fun fixedUpdate() {
        if(useGravity) {
            applyForce(Physics.gravity * mass)
        }
    }

    fun applyForce(force: Vector, displacement: Point = Point.ZERO, time: Double = Time.fixedDeltaTime) {
        PhysicsObject.getPhysicsObject(this).applyForce(force, (gameObject!!.getGlobalPosition() + displacement.asVector2()).toPoint(), time)
    }
}