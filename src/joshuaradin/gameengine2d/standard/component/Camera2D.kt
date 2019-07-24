package joshuaradin.gameengine2d.standard.component

import joshuaradin.gameengine2d.core.ObjectBehavior

class Camera2D : ObjectBehavior(){

    override fun start() {
        addComponent<FrameRateLimiter>()
    }
}