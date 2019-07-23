package joshuaradin.gameengine2d.user.output

import joshuaradin.gameengine2d.core.GameObjectTracker
import joshuaradin.gameengine2d.standard.component.Camera2D
import joshuaradin.gameengine2d.standard.component.Transform
import joshuaradin.gameengine2d.user.input.Input
import java.awt.*
import javax.swing.JFrame

class GameWindow(name: String, val gameObjectTracker: GameObjectTracker = GameObjectTracker.initialize()) : JFrame(name) {

    init {
        Input.attachTo(this)
        preferredSize = Dimension(600, 400)
        defaultCloseOperation = EXIT_ON_CLOSE
        contentPane.layout = BorderLayout()
        /*val jLabel = JLabel("Hello World")
        jLabel.horizontalTextPosition = JLabel.CENTER
        jLabel.foreground = Color.RED
        contentPane.add(jLabel, BorderLayout.NORTH)*/
        contentPane.add(GamePanel().mainPanel)
        isResizable = false
        pack()
        isVisible = true
    }


    var currentCamera: Camera2D? = null

    /**
     * {@inheritDoc}
     *
     * @since 1.7
     */
    override fun paint(g: Graphics?) {
        super.paint(g)
        paint(g as Graphics2D)
    }

    private fun paint(g2D: Graphics2D) {
        fun I2DRenderer.render(transform: Transform, camera2D: Camera2D) {
            render(g2D, transform, camera2D, this@GameWindow)
        }

        for (activeObject in gameObjectTracker.activeObjects.filter { it.hasComponent<Renderer2DComponent>() }) {
            val renderer: I2DRenderer = activeObject.getComponent<Renderer2DComponent>() as I2DRenderer
            val transform = activeObject.getComponent<Transform>()
            if(transform != null && currentCamera != null)
                renderer.render(transform, currentCamera!!)
        }
    }
}