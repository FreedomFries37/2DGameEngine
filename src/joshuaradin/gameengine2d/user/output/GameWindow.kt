package joshuaradin.gameengine2d.user.output

import joshuaradin.gameengine2d.user.input.Input
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JLabel

class GameWindow(name: String) : JFrame(name) {

    init {
        Input.attachTo(this)
        preferredSize = Dimension(600, 400)
        defaultCloseOperation = EXIT_ON_CLOSE
        contentPane.layout = BorderLayout()
        val jLabel = JLabel("Hello World")
        jLabel.horizontalTextPosition = JLabel.CENTER
        jLabel.foreground = Color.RED
        contentPane.add(jLabel, BorderLayout.NORTH)
        isResizable = false
        pack()
        isVisible = true
    }


}