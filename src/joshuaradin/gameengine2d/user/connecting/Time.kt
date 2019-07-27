package joshuaradin.gameengine2d.user.connecting

import kotlin.math.pow

object Time {

    val startTime = System.nanoTime()

    var deltaTime: Double = 0.0
    val totalTime: Double get() {
        val l = System.nanoTime() - startTime
        return l / 10.0.pow(9)
    }
}