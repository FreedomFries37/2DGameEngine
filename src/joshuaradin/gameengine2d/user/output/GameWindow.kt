package joshuaradin.gameengine2d.user.output

import joshuaradin.gameengine2d.core.service.GameObjectTracker
import joshuaradin.gameengine2d.standard.component.Camera2D
import joshuaradin.gameengine2d.user.connecting.Screen
import joshuaradin.gameengine2d.user.input.Input
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JLayeredPane

class GameWindow(name: String, dimension: Dimension, gameObjectTracker: GameObjectTracker = GameObjectTracker.initialize()) : JFrame(name) {

    val mainPanel: RenderPanel

    var gameObjectTracker: GameObjectTracker?
        get() = mainPanel.gameObjectTracker
        set(value) {
            mainPanel.gameObjectTracker = value
        }
    var currentCamera: Camera2D?
        get() = mainPanel.currentCamera
        set(value) {
            mainPanel.currentCamera = value
        }

    fun JLayeredPane.addO(comp: Component, index: Int){
        add(comp, index)
        comp.setBounds(0, 0, this.width, this.height)
    }


    init {
        Input.attachTo(this)
        preferredSize = dimension
        defaultCloseOperation = EXIT_ON_CLOSE
        contentPane.layout = BorderLayout()




        mainPanel = RenderPanel()

        this.gameObjectTracker = gameObjectTracker
        //mainPanel.isDoubleBuffered = true
        add(mainPanel)
        //layeredPane.addO(guiPanel, 1)
        //add(layeredPane)

        isResizable = false


        pack()
        isVisible = true

        Screen(width, height)
    }



}