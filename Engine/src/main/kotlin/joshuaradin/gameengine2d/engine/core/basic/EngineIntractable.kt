package joshuaradin.gameengine2d.engine.core.basic

abstract class EngineIntractable {
    var enabled: Boolean = true
    internal var editorOnly: Boolean = false


    abstract fun awake()

    abstract fun start()

    abstract fun update()

    abstract fun fixedUpdate()

    abstract fun onBoundaryEnter(other: GameObject)

    abstract fun onBoundaryStay(other: GameObject)

    abstract fun onBoundaryExit(other: GameObject)

    abstract fun onMouseClick()

    abstract fun onMouseDown()

    abstract fun onMouseUp()

    abstract fun onMouseStay()

    abstract fun onMouseExit()
}