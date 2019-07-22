package joshuaradin.gameengine2d.core

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

    override fun init() {

    }

    override fun start() {

    }

    override fun update() {

    }
}