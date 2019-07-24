package joshuaradin.gameengine2d.user.connecting

class Screen(width: Int, height: Int) {

    companion object {
        var width: Int = 0
            internal set

        var height: Int = 0
            internal set
    }

    init {
        Screen.width = width
        Screen.height = height
    }
}