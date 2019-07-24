package joshuaradin.gameengine2d.core.asset

import joshuaradin.gameengine2d.core.basic.GameObject
import java.io.File

class GameObjectAssetFactory : IAssetFactory<GameObject> {

    override fun toFile(o: GameObject, filename: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fromFile(file: File): GameObject? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFileName(o: GameObject): String {
        return "${o.name}.gea"
    }
}