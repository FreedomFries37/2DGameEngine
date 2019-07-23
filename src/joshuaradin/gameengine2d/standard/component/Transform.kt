package joshuaradin.gameengine2d.standard.component

import joshuaradin.gameengine2d.core.GameObject
import joshuaradin.gameengine2d.core.ObjectBehavior
import joshuaradin.gameengine2d.standard.type.Line
import joshuaradin.gameengine2d.standard.type.Point
import joshuaradin.gameengine2d.standard.type.Rotation
import joshuaradin.gameengine2d.standard.type.Vector2

class Transform : ObjectBehavior() {

    private class State constructor(val position: Vector2, val scale: Vector2, val rotation: Rotation)

    var isLocal = true

    private var _position = Vector2(0.0, 0.0)
    var position: Vector2
        get() = _position
        set(value){
            val prevState = currentState()
            _position = value
            val currentState = currentState()
            val children = gameObject?.getChildren() ?: listOf()
            for (child in children) {
                child.cascadeTransformation(prevState, currentState)
            }
        }


    private var _scale = Vector2(0.0, 0.0)
    private var _rotation = Rotation(0.0)



    private fun currentState() : State{
        return State(_position, _scale, _rotation)
    }

    private fun cascadeTransformation(parentPrevState: State, parentCurrentState: State){
        if(!isLocal) return
        val myState = currentState()

        val rotDifference = parentCurrentState.rotation - parentPrevState.rotation
        val parentOldCenter = parentPrevState.position

        var line = Line(Point.ZERO, myState.position.toPoint())

        val radialDistance = line.length

    }

    private fun GameObject.cascadeTransformation(parentPrevState: State, parentCurrentState: State){
        this.getComponent<Transform>()?.cascadeTransformation(parentPrevState, parentCurrentState)
    }


}