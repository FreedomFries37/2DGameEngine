package joshuaradin.gameengine2d.standard.component

import joshuaradin.gameengine2d.core.basic.GameObject
import joshuaradin.gameengine2d.core.basic.ObjectBehavior
import joshuaradin.gameengine2d.core.service.GameObjectTracker
import joshuaradin.gameengine2d.standard.type.geometry.*
import joshuaradin.gameengine2d.user.connecting.Screen
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

open class InterferenceBoundary : ObjectBehavior(), MouseListener, IBoundary {

    var isTrigger = true
    var boundary: Shape = Square(Point.ZERO)
    set(value) {
        field = value
        lowDefBoundary = LowDefBoundary(boundary)
    }
    private var lowDefBoundary = LowDefBoundary(boundary)

    private val currentCollided = mutableSetOf<GameObject>()



    override fun start() {
        boundary += transform!!.position
        Screen.frame?.mainPanel?.addMouseListener(this)
    }

    override fun update() {


        if(isTrigger) {
            for (gameObject in currentCollided) {
                gameObject.onBoundaryStay(this.gameObject!!)
            }

            for (collider in GameObjectTracker.instance.colliders().filter { it.gameObject !in currentCollided }) {
                if(inBounds(collider.boundary)) {
                    val element = collider.gameObject!!
                    currentCollided.add(element)
                    element.onBoundaryEnter(this.gameObject!!)
                }
            }

            for (colliders in currentCollided.map { it.getComponents<InterferenceBoundary>().toList() }) {
                var atleastOne = false
                val connected: GameObject = colliders[0].gameObject!!
                for (collider: InterferenceBoundary in colliders) {
                    if(boundary.inBounds(collider.boundary)) {
                        atleastOne = true
                    }
                }


                if(!atleastOne) {
                    currentCollided.remove(connected)
                    connected.onBoundaryExit(this.gameObject!!)
                }
            }

        }
    }

    fun java.awt.Point.to2DPoint() : Point {
        return Point(x, y)
    }

    /**
     * Invoked when the mouse enters a component.
     * @param e the event to be processed
     */
    override fun mouseEntered(e: MouseEvent?) { }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     * @param e the event to be processed
     */
    override fun mouseClicked(e: MouseEvent?) {
        val (x, y) = e?.point!!.to2DPoint() - Screen.center().asVector2()
        val effectiveGlobalPosition: Point
        val (cgX, cgY) = Screen.camera()?.gameObject!!.getGlobalPosition()
        effectiveGlobalPosition = Point(x + cgX, -y + cgY)


        if(enabled && boundary.inBounds(effectiveGlobalPosition - gameObject!!.getGlobalPosition())){
            gameObject?.onMouseClick()
        }
    }

    /**
     * Invoked when the mouse exits a component.
     * @param e the event to be processed
     */
    override fun mouseExited(e: MouseEvent?) { }

    /**
     * Invoked when a mouse button has been released on a component.
     * @param e the event to be processed
     */
    override fun mouseReleased(e: MouseEvent?) {
        val (x, y) = e?.point!!.to2DPoint() - Screen.center().asVector2()
        val effectiveGlobalPosition: Point
        val (cgX, cgY) = Screen.camera()?.gameObject!!.getGlobalPosition()
        effectiveGlobalPosition = Point(x - cgX, y - cgY)


        if(enabled && boundary.inBounds(effectiveGlobalPosition - gameObject!!.getGlobalPosition())){
            gameObject?.onMouseUp()
        }
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     * @param e the event to be processed
     */
    override fun mousePressed(e: MouseEvent?) {
        val (x, y) = e?.point!!.to2DPoint() - Screen.center().asVector2()
        val effectiveGlobalPosition: Point
        val (cgX, cgY) = Screen.camera()?.gameObject!!.getGlobalPosition()
        effectiveGlobalPosition = Point(x - cgX, y - cgY)


        if(enabled && boundary.inBounds(effectiveGlobalPosition - gameObject!!.getGlobalPosition())){
            gameObject?.onMouseDown()
        }
    }

    override fun inBounds(point: Point): Boolean {
        if(!lowDefBoundary.inBounds(point)) return false
        return boundary.inBounds(point)
    }

    override fun inBounds(shape: Shape): Boolean {
        if(!lowDefBoundary.inBounds(shape)) return false
        return boundary.inBounds(shape)
    }

    override fun toString(): String {
        return "Inbound Objects = $currentCollided"
    }
}