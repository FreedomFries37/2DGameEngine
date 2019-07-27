package joshuaradin.gameengine2d.core.basic

import joshuaradin.gameengine2d.core.listeners.*

abstract class ObjectBehavior : Component(), IMouseListener {

    private var mouseListener: IMouseListener? = null

    final override fun reactToEvent(e: Event) {
        when(e){
            is MouseEvent -> {
                if(e == MouseDown) {
                    if (mouseListener != null) mouseListener?.onMouseDown()
                    else onMouseDown()
                }
                else if(e == MouseUp) {
                    if (mouseListener != null) mouseListener?.onMouseUp()
                    else onMouseUp()
                }
            }
        }
    }

    override fun onMouseDown() {

    }

    override fun onMouseUp() {

    }

    override fun onMouseClick() {

    }

    override fun onMouseStay() {

    }

    override fun onMouseExit() {

    }

    override fun init() {

    }

    override fun start() {

    }

    override fun update() {

    }

    override fun onBoundaryEnter(other: GameObject){

    }

    override fun onBoundaryExit(other: GameObject){

    }

    override fun onBoundaryStay(other: GameObject) {

    }

    override fun toString(): String {
        return ""
    }
}