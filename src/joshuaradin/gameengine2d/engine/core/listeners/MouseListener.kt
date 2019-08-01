package joshuaradin.gameengine2d.engine.core.listeners


abstract class MouseEvent protected constructor(msg: String) : Event(msg)
object MouseDown : MouseEvent("Mouse Down")
object MouseUp : MouseEvent("Mouse Up")

interface IMouseListener : IListener {

    fun onMouseDown()
    fun onMouseUp()
}