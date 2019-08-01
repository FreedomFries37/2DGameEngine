package joshuaradin.gameengine2d.user.output

import joshuaradin.gameengine2d.engine.core.basic.ObjectBehavior
import joshuaradin.gameengine2d.engine.standard.component.Camera2D
import joshuaradin.gameengine2d.engine.standard.component.Transform
import joshuaradin.gameengine2d.user.connecting.Screen
import java.awt.Graphics2D
import java.awt.image.ImageObserver

interface I2DRenderer {


    /**
     *
     * @param g Graphics
     * @param transform transform of the related [GameObject]
     * @param camera2D the camera object
     * @param observer the image observer
     *
     */
    fun render(
        g: Graphics2D,
        transform: Transform,
        camera2D: Camera2D,
        observer: ImageObserver
    )


}

abstract class Renderer2DComponent : ObjectBehavior(), I2DRenderer {
    var level = 0

    protected fun getDistanceFromCamera(transform: Transform, camera2D: Camera2D) =
        (transform.gameObject!!.getGlobalPosition() - camera2D.gameObject!!.getGlobalPosition()) + Screen.center().asVector2()

}