package joshuaradin.gameengine2d.engine.standard.component.rendering

import joshuaradin.gameengine2d.engine.standard.component.Camera2D
import joshuaradin.gameengine2d.engine.standard.component.Transform
import joshuaradin.gameengine2d.user.output.Renderer2DComponent
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.awt.image.ImageObserver
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.math.roundToInt

class SpriteRenderer : Renderer2DComponent() {

    var path: String = ""

    override fun render(
        g: Graphics2D,
        transform: Transform,
        camera2D: Camera2D,
        observer: ImageObserver
    ) {
        if(path.isEmpty()) return
        var image: BufferedImage

        try{
            image = ImageIO.read(File(path))
        }catch (e: IOException) {
            return
        }


        g.drawImage(image, transform.position.x.roundToInt(), transform.position.y.roundToInt(), observer)
    }


}