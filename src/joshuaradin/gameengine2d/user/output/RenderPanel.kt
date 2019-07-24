package joshuaradin.gameengine2d.user.output

import joshuaradin.gameengine2d.core.GameObjectTracker
import joshuaradin.gameengine2d.standard.component.Camera2D
import joshuaradin.gameengine2d.standard.component.Transform
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel

class RenderPanel : JPanel(){

    var gameObjectTracker: GameObjectTracker? = null
    var currentCamera: Camera2D? = null

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        paintComponent(g as Graphics2D)
    }

    private fun paintComponent(g2D: Graphics2D) {

        if(gameObjectTracker == null) return

        for (activeObject in gameObjectTracker!!.getRenderables()) {
            val renderer: I2DRenderer = activeObject.getComponent<Renderer2DComponent>() as I2DRenderer
            val transform = activeObject.getComponent<Transform>()
            if(transform != null && currentCamera != null)
                renderer.render(g2D, transform, currentCamera!!, this)
        }

        g2D.drawString("Bingo", 50, 50)
    }
}