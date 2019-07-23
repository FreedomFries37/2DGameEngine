package joshuaradin.gameengine2d.standard.type

class Line (var u: Point, var v: Point) {
    val length: Double
        get() = u.distance(v)
}
