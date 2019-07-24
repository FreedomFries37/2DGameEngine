package joshuaradin.gameengine2d.core.asset

import java.io.File

interface IAssetFactory<T> {

    fun toFile(o : T, filename: String)

    @Throws(java.io.IOException::class)
    fun fromFile(file: File) : T?

    fun getFileName(o: T) : String

    fun toAsset(o: T) : Asset<T> {
        return Asset(this, getFileName(o), o)
    }
}