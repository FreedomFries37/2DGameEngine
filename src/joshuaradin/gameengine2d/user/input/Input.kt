package joshuaradin.gameengine2d.user.input

import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JFrame

object Input {

    private object Listener : KeyListener {
        /**
         * Invoked when a key has been typed.
         * See the class description for [KeyEvent] for a definition of
         * a key typed event.
         * @param e the event to be processed
         */
        override fun keyTyped(e: KeyEvent?) {

        }

        /**
         * Invoked when a key has been pressed.
         * See the class description for [KeyEvent] for a definition of
         * a key pressed event.
         * @param e the event to be processed
         */
        override fun keyPressed(e: KeyEvent?) {
            if(e != null) keysPressed.add(e.keyCode)
        }

        /**
         * Invoked when a key has been released.
         * See the class description for [KeyEvent] for a definition of
         * a key released event.
         * @param e the event to be processed
         */
        override fun keyReleased(e: KeyEvent?) {
            if(e != null) keysPressed.remove(e.keyCode)
        }
    }

    private val keysPressed = mutableSetOf<Int>()

    fun getKeyDown(keycode: Int) : Boolean{
        return keysPressed.contains(keycode)
    }

    fun attachTo(o: JFrame) {
        o.addKeyListener(Listener)
    }

}