package joshuaradin.gameengine2d.engine.standard.component

import joshuaradin.gameengine2d.engine.core.basic.ObjectBehavior

class FrameRateLimiter : ObjectBehavior() {


    var limiter: Long = 1000/60

    override fun update() {
        Thread.sleep(limiter)
    }
}