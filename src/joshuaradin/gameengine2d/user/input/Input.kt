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
                if(!keysChecked.containsKey(e.keyCode)) keysChecked[e.keyCode] = false
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
                keysChecked.remove(e.keyCode)
            }
        }
    }


    private val keysPressed = mutableSetOf<Int>()
    private val keysChecked = mutableMapOf<Int, Boolean>()

    /**
     * returns true while a key is being held
     */
    fun getKey(keycode: Int) : Boolean{
        return keysPressed.contains(keycode)
    }

    /**
     * returns true while a key is being held
     */
    fun getKey(keycode: KeyCode) : Boolean{
        return keysPressed.contains(keycode.code)
    }

    /**
     * only returns true once for when a key is pressed
     */
    fun getKeyDown(keycode: Int) : Boolean{
        if(!keysChecked.containsKey(keycode)) return false
        val output = !(keysChecked[keycode] ?: true)
        keysChecked[keycode] = true
        return output
    }

    /**
     * only returns true once for when a key is pressed
     */
    fun getKeyDown(keycode: KeyCode) : Boolean{
        return getKeyDown(keycode.code)
    }

    fun attachTo(o: JFrame) {
        o.addKeyListener(Listener)
    }

}