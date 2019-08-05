package joshuaradin.gameengine2d.test

import joshuaradin.gameengine2d.engine.core.basic.GameObject
import joshuaradin.gameengine2d.engine.core.basic.ObjectBehavior
import joshuaradin.gameengine2d.engine.standard.component.physics.RigidBody2D
import joshuaradin.gameengine2d.engine.standard.component.rendering.ShapeRenderer
import joshuaradin.gameengine2d.engine.standard.type.Vector2
import joshuaradin.gameengine2d.engine.standard.type.geometry.Point
import joshuaradin.gameengine2d.engine.standard.type.geometry.Square
import java.awt.Color

class PhysicsScript : ObjectBehavior() {

    var modelObject: GameObject? = null

    var object1: GameObject? = null
    var object2: GameObject? = null

    var object1Height: Double = 0.0

    override fun awake() {
        modelObject = GameObject.createEmpty(null)
        modelObject!!.addComponent<RigidBody2D>()
        val renderer = modelObject!!.addComponent<ShapeRenderer>()
        renderer!!.shape = Square(Point.ZERO, 1.0)
        renderer.fillColor = Color.CYAN
    }

    override fun start() {
        object1 = gameObject!!.instantiate(modelObject!!)
        object1!!.transform.position = Vector2(-20, 0)
        object1!!.getComponent<RigidBody2D>()?.useGravity = true
        object1!!.getComponent<RigidBody2D>()?.mass = 10.0
        object1Height = object1!!.transform.position.y

        object2 = gameObject!!.instantiate(modelObject!!)
        object2!!.transform.position = Vector2(20, 0)
        object2!!.getComponent<RigidBody2D>()?.useGravity = true
        object2!!.getComponent<RigidBody2D>()?.mass = 20.0
    }

    override fun fixedUpdate() {
        val currentHeight = object1!!.transform.position.y
        val diff = currentHeight - object1Height
        //println("${diff / Time.fixedDeltaTime} units per sec")
        object1Height = currentHeight
    }
}