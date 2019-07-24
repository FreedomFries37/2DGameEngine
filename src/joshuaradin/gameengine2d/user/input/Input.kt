package joshuaradin.gameengine2d.user.input

import javafx.scene.input.KeyCode
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
            if(e != null){
                if(!keysPressed.contains(e.keyCode) ) keysPressed.add(e.keyCode)
                if(!keysDown.contains(e.keyCode) ) keysDown.add(e.keyCode)
                if(!keysPressed.contains(e.keyCode) && keysChecked.contains(e.keyCode)) keysChecked.remove(e.keyCode)
            }
        }

        /**
         * Invoked when a key has been released.
         * See the class description for [KeyEvent] for a definition of
         * a key released event.
         * @param e the event to be processed
         */
        override fun keyReleased(e: KeyEvent?) {
            if(e != null){
                keysPressed.remove(e.keyCode)
            }
        }
    }

    //TODO: make getKeyDown() functional
    private val keysPressed = mutableSetOf<Int>()
    private val keysDown = mutableSetOf<Int>()
    private val keysChecked = mutableSetOf<Int>()

    fun getKey(keycode: Int) : Boolean{
        return keysPressed.contains(keycode)
    }

    fun getKey(keycode: KeyCode) : Boolean{
        return keysPressed.contains(keycode.code)
    }

    fun getKeyDown(keycode: Int) : Boolean{
        val output = keysDown.contains(keycode) && !keysChecked.contains(keycode)
        if(output){
            keysDown.remove(keycode)
            keysChecked.add(keycode)
        }
        return output
    }

    fun getKeyDown(keycode: KeyCode) : Boolean{
        return getKeyDown(keycode.code)
    }

    fun attachTo(o: JFrame) {
        o.addKeyListener(Listener)
    }

}