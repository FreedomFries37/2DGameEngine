package joshuaradin.gameengine2d.engine.standard.type.geometry

interface IBoundary {

    fun inBounds(point: Point) : Boolean
    fun inBounds(shape: Shape) : Boolean
    fun outOfBounds(point: Point) : Boolean = !inBounds(point)
    fun outOfBounds(shape: Shape) : Boolean = !inBounds(shape)

}