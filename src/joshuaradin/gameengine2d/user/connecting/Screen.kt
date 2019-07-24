package joshuaradin.gameengine2d.user.connecting

import joshuaradin.gameengine2d.standard.type.Point

class Screen(width: Int, height: Int) {

    companion object {
        var width: Int = 0
            internal set

        var height: Int = 0
            internal set

        fun center() = Point(width/2, height/2)
    }

    init {
        Screen.width = width
        Screen.height = height
    }


}