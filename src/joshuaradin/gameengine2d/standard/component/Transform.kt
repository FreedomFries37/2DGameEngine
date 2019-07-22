package joshuaradin.gameengine2d.standard.component

import joshuaradin.gameengine2d.core.GameObject
import joshuaradin.gameengine2d.core.ObjectBehavior
import joshuaradin.gameengine2d.standard.type.Rotation
import joshuaradin.gameengine2d.standard.type.Vector2

class Transform(gameObject: GameObject) : ObjectBehavior() {

    var position = Vector2(0.0, 0.0)
    var scale = Vector2(0.0, 0.0)
    var rotation = Rotation(0.0)


}