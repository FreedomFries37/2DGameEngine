package joshuaradin.gameengine2d.standard.component

import joshuaradin.gameengine2d.core.basic.GameObject
import joshuaradin.gameengine2d.core.basic.ObjectBehavior
import joshuaradin.gameengine2d.core.service.GameObjectTracker
import joshuaradin.gameengine2d.standard.type.Point
import joshuaradin.gameengine2d.standard.type.Shape
import joshuaradin.gameengine2d.standard.type.Square

class InterferenceBoundry : ObjectBehavior() {

    var isTrigger = false
    var boundry: Shape = Square(Point.ZERO)

    private val currentCollided = mutableSetOf<GameObject>()

    override fun start() {
        boundry += transform!!.position
    }

    override fun update() {
        if(isTrigger) {
            for (gameObject in currentCollided) {
                gameObject.onBoundryStay(this.gameObject!!)
            }

            for (collider in GameObjectTracker.instance.colliders().filter { it.gameObject !in currentCollided }) {
                if(boundry.inBounds(collider.boundry)) {
                    val element = collider.gameObject!!
                    currentCollided.add(element)
                    element.onBoundryEnter(this.gameObject!!)
                }
            }

            for (colliders in currentCollided.map { it.getComponents<InterferenceBoundry>().toList() }) {
                var atleastOne = false
                val connected: GameObject = colliders[0].gameObject!!
                for (collider: InterferenceBoundry in colliders) {
                    if(boundry.inBounds(collider.boundry)) {
                        atleastOne = true
                    }
                }


                if(!atleastOne) {
                    currentCollided.remove(connected)
                    connected.onBoundryExit(this.gameObject!!)
                }
            }

        } else {

        }
    }
}