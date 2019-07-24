package joshuaradin.gameengine2d.standard.component

import joshuaradin.gameengine2d.core.basic.ObjectBehavior

class FrameRateLimiter : ObjectBehavior() {


    var limiter: Long = 1000/60

    override fun update() {
        Thread.sleep(limiter)
    }
}