package joshuaradin.gameengine2d.engine.standard.component

import joshuaradin.gameengine2d.engine.core.basic.GameObject
import joshuaradin.gameengine2d.engine.core.basic.ObjectBehavior
import joshuaradin.gameengine2d.engine.standard.type.Vector2
import joshuaradin.gameengine2d.engine.standard.type.geometry.Line
import joshuaradin.gameengine2d.engine.standard.type.geometry.Point
import joshuaradin.gameengine2d.engine.standard.type.geometry.Rotation
import java.lang.Math.cos
import java.lang.Math.sin

class Transform : ObjectBehavior() {

    private class State constructor(val position: Vector2, val scale: Vector2, val rotation: Rotation)

    private var _isLocal = true
    var isLocal: Boolean
        get() = _isLocal
        set(value) {
            if(_isLocal == value) return
            if(value) { // from global to local
                val globalPosition = gameObject!!.parent!!.getGlobalPosition()
                applyGlobalToLocal(globalPosition)
            }else{
                position = gameObject!!.getGlobalPosition()
            }
            _isLocal = value
        }

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


    private var _scale = Vector2(1.0, 1.0)
    var scale: Vector2
        get() = _scale
        set(value){
            val prevState = currentState()
            _scale = value
            val currentState = currentState()
            val children = gameObject?.getChildren() ?: listOf()
            for (child in children) {
                child.cascadeTransformation(prevState, currentState)
            }
        }
    private var _rotation = Rotation(0.0)
    var rotation: Rotation
        get() = _rotation
        set(value){
            val prevState = currentState()
            _rotation = value
            val currentState = currentState()
            val children = gameObject?.getChildren() ?: listOf()
            for (child in children) {
                child.cascadeTransformation(prevState, currentState)
            }
        }



    private fun currentState() : State{
        return State(_position, _scale, _rotation)
    }

    private fun State.apply() {
        this@Transform.position = position
        this@Transform.rotation = rotation
        this@Transform.scale = scale
    }

    private fun cascadeTransformation(parentPrevState: State, parentCurrentState: State){
        if(!isLocal) return
        val myState = currentState()

        val rotDifference = parentCurrentState.rotation - parentPrevState.rotation
        val parentOldCenter = parentPrevState.position
        val parentNewCenter = parentCurrentState.position

        val line = Line(
            Point.ZERO,
            myState.position.toPoint()
        )

        var radialDistance = line.length
        val newRot: Rotation = myState.rotation + rotDifference



        val scaleDifference = parentCurrentState.scale - parentPrevState.scale
        val scaleIncreasing = scaleDifference + 1.0
        val newScale: Vector2 = scaleIncreasing*myState.scale


        val angleFromCenter = parentOldCenter.toPoint().angle(myState.position.toPoint())
        val newX = radialDistance*newScale.x*cos(angleFromCenter + rotDifference.radians)
        val newY = radialDistance*newScale.y*sin(angleFromCenter + rotDifference.radians)

        val parentChangeVector = parentNewCenter - parentOldCenter
        val newPosition: Vector2 = (Vector2(newX, newY) + parentChangeVector)

        State(newPosition, newScale, newRot).apply()
    }

    private fun GameObject.cascadeTransformation(parentPrevState: State, parentCurrentState: State){
        this.getComponent<Transform>()?.cascadeTransformation(parentPrevState, parentCurrentState)
    }

    fun applyGlobalToLocal(destGlobal: Vector2){
        val newPosition = this.position - destGlobal
        this.position = newPosition
    }



    override fun toString(): String {
        return "position=$_position, scale=$_scale, rotation=$_rotation)"
    }


}