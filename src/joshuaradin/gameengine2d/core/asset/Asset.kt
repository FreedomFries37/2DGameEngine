package joshuaradin.gameengine2d.core.asset

import java.io.File
import java.io.IOException

open class Asset<T> constructor(private val factory: IAssetFactory<T>, private val filename: String, obj: T? = null) {
    val obj: T = if (obj == null) factory.fromFile(File(filename))!! else obj

    fun loadToFile() : Boolean{
        if(obj == null) return false
        return try{
            factory.toFile(obj, filename)
            true
        }catch (e: IOException) {
            false
        }
    }
}