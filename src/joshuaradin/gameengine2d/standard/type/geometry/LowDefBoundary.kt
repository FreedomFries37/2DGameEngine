package joshuaradin.gameengine2d.standard.type.geometry

class LowDefBoundary(shape: Shape) : IBoundary{

    val center: Point = shape.center
    val radius: Double

    init {
        var max = 0.0
        for (point in shape.points) {
            val distance = point.distance(center)
            if(distance > max) max = distance
        }
        radius = max
    }


    override fun inBounds(point: Point): Boolean {
        return point.distance(center) < radius
    }

    override fun inBounds(shape: Shape): Boolean {
        return shape.points.any { inBounds(it) }
    }
}