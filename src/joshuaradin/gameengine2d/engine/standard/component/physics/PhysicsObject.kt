package joshuaradin.gameengine2d.engine.standard.component.physics

import joshuaradin.gameengine2d.engine.core.basic.GameObject
import joshuaradin.gameengine2d.engine.standard.type.Vector
import joshuaradin.gameengine2d.engine.standard.type.geometry.Line
import joshuaradin.gameengine2d.engine.standard.type.geometry.Point
import joshuaradin.gameengine2d.engine.standard.type.geometry.Rotation
import kotlin.math.pow


/**
 * Represents the total inertia of an [GameObject]
 *
 * @param gameObject the base object to attach to
 */
internal class PhysicsObject private constructor(val gameObject: GameObject)  {
    companion object {
        /**
         * a map of all of the existing physics objects
         */
        private val objectMap = mutableMapOf<GameObject, PhysicsObject>()

        /**
         * Get all of the existing physics objects
         */
        fun getPhysicsObjects() : Collection<PhysicsObject> = objectMap.values

        /**
         * Get the physics object for a [RigidBody2D]
         * Determined by the highest level continuous chain of rigid bodies within parent GameObjects
         *
         * @param rgb the rigid body to create a physics object for
         * @return a [PhysicsObject]
         */
        fun getPhysicsObject(rgb: RigidBody2D) : PhysicsObject {
            val gameObject = rgb.gameObject
            val upperMostPhysicsGameObject = gameObject!!.lastInChain()
            if(objectMap.containsKey(upperMostPhysicsGameObject)){
                val physicsObject = objectMap[upperMostPhysicsGameObject]!!
                if(objectMap.containsKey(gameObject) && objectMap[gameObject]!! != physicsObject) {
                    objectMap[gameObject]?.involved?.remove(gameObject)
                }

                if(!objectMap.containsKey(gameObject)) {
                    objectMap[gameObject] = physicsObject
                    physicsObject.involved.add(gameObject)
                }
                return physicsObject
            }

            val output = PhysicsObject(gameObject)

            objectMap[gameObject] = output


            return output
        }

        /**
         * Reattaches rigid bodies to to the correct [PhysicsObject], in case they get out of sync
         */
        fun validatePhysicsObjects() {
            for (gO in objectMap.keys) {
                getPhysicsObject(gO.getComponent()!!)
            }
        }

        /**
         * @return the upper most parent [GameObject] with a [RigidBody2D]
         */
        private fun GameObject.lastInChain() : GameObject? {
            var output: GameObject? = this
            if(output == null || !output.hasComponent<RigidBody2D>()) return null
            while (output!!.parent != null && output.parent!!.hasComponent<RigidBody2D>()) {
                output = output.parent
            }
            return output
        }
    }

    /**
     * Shortcut to get the mass of a [RigidBody2D]
     */
    private val GameObject.mass: Double get() {
        return getComponent<RigidBody2D>()!!.mass
    }



    private val involved: MutableList<GameObject> = mutableListOf(gameObject)
    private val mass: Double get() {
        var output = 0.0
        for (gameObject in involvedFiltered()) {
            output += gameObject.mass
        }
        return output
    }

    private fun involvedFiltered() = involved.filter { it.enabled && it.getComponent<RigidBody2D>()?.enabled ?: false }
    private val centerOfMass: Point get() {
        var distanceMassSum = Point.ZERO
        var massSum = 0.0
        for (gameObject in involvedFiltered()) {
            distanceMassSum += gameObject.transform.position * gameObject.mass
        }
        return distanceMassSum / massSum
    }
    private val inertia: Double get() {
        return involvedFiltered().sumByDouble { it.getComponent<RigidBody2D>()!!.mass * centerOfMass.distance(it.transform.position.toPoint()).pow(2) }
    }

    private var momentum: Momentum = Vector(0.0)
    private var angularMomentum: Double = 0.0

    val velocity: Vector
        get() {
            return momentum / mass
        }

    val angularVelocity: Rotation
        get() {
            return Rotation(angularMomentum / inertia)
        }

    fun applyForce(force: Force, globalPosition: Point, time: Double) {
        val changeInMomentum = force * time // Delta P
        momentum += changeInMomentum

        val lineToForce = Line(globalPosition, centerOfMass)
        val effectiveForceLine = Line(globalPosition, globalPosition + force.asVector2())

        val angle = lineToForce.angleWith(effectiveForceLine)
        val tangentialForce = angle!!.sin() * force.displacement
        val torque = tangentialForce * lineToForce.length
        val changeInAngularMomentum = torque * time
        angularMomentum += changeInAngularMomentum
    }

}