package joshuaradin.gameengine2d.engine.standard.component.physics

import joshuaradin.gameengine2d.engine.standard.type.Vector
import joshuaradin.gameengine2d.engine.standard.type.geometry.Rotation
import joshuaradin.gameengine2d.user.connecting.Time


internal typealias Force = Vector
internal typealias Momentum = Vector
object Physics {

    var gravity: Vector = Vector(9.81, Rotation.DOWN)

    fun updatePhysics() {
        for (physicsObject in PhysicsObject.getPhysicsObjects()) {

            physicsObject.gameObject.transform.position += (physicsObject.velocity * Time.fixedDeltaTime).asVector2()
            physicsObject.gameObject.transform.rotation += (physicsObject.angularVelocity * Time.fixedDeltaTime)
        }
    }
}