package joshuaradin.gameengine2d.user.output

import joshuaradin.gameengine2d.core.ObjectBehavior
import joshuaradin.gameengine2d.standard.component.Camera2D
import joshuaradin.gameengine2d.standard.component.Transform
import java.awt.Graphics2D
import java.awt.image.ImageObserver

interface I2DRenderer {
    fun render(
        g: Graphics2D,
        transform: Transform,
        camera2D: Camera2D,
        observer: ImageObserver
    )
}

abstract class Renderer2DComponent : ObjectBehavior(), I2DRenderer