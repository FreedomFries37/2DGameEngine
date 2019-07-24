package joshuaradin.gameengine2d.standard.type

import kotlin.math.*

class Triangle(p1: Point, p2: Point, p3: Point) : Shape(p1, p2 , p3)
open class Quadrilateral(p1: Point, p2: Point, p3: Point, p4: Point) : Shape(p1, p2, p3, p4) {
    internal constructor(other: Shape) : this(other.points[0], other.points[1], other.points[2], other.points[3])
}
open class Parallelogram(center: Point, tops: Double, sides: Double, acuteAngle: Rotation) : Quadrilateral(createParallelogram(center, tops, sides, acuteAngle))
open class Rectangle(center: Point, tops: Double, sides: Double) : Parallelogram(center, tops, sides, Rotation.createDeg(90.0))
class Square(center: Point, size: Double = 1.0) : Rectangle(center, size, size)

private fun createParallelogram(center: Point, tops: Double, sides: Double, acuteAngle: Rotation) : Shape {
    val p1 = center.copy(y =  sides*sin(acuteAngle.radians))
    val p2 = p1.copy(x = p1.x + tops)
    val nextY = p2.y + sides*sin(-acuteAngle.radians)
    val nextX = p2.x + sides*cos(-acuteAngle.radians)
    val p3 = Point(nextX, nextY)
    val p4 = p3.copy(x = p3.x - tops)

    val recentered = Point.recenter(p1, p2, p3, p4)
    var output: Shape = Quadrilateral(recentered[0], recentered[1], recentered[2], recentered[3])
    output = output.recenter(Point.ZERO.asVector2())
    return output
}

class Circle internal constructor(points: List<Point>) : Shape(points) {
    constructor(center: Point, radius: Double, accuracy: Double = defaultCircleAccuracy) : this(createPointSet(center, radius, accuracy))

    companion object{
        var defaultCircleAccuracy: Double = .999
        val maxCircleAccuracy: Double = getAccuracy(7)

    }
}

fun getAccuracy(numberOfPoints: Int, radius: Double = 1.0) : Double {
    val theta = PI/numberOfPoints
    val D = sin(theta)*radius
    val triangeArea = D*radius
    val sumTriangles = triangeArea*numberOfPoints
    val actual = PI * radius.pow(2)
    return sumTriangles / actual
}

fun getAccuracy(significantDigits: Int) : Double{
    val actual = PI
    var numPoints = 3

    var c: Circle
    var createdArea: Double
    val center = Point(0, 0)
    do {
        numPoints *= 2
        c = Circle(pointFactory(numPoints, center, 1.0))
        createdArea = c.area

    } while (!nFirstDigitsSame(significantDigits, actual, createdArea))



    return getAccuracy(numPoints, 1.0)
}

private fun nFirstDigitsSame(n: Int, a: Number, b: Number) : Boolean{


    val tempA = a.toString()
    val tempB = b.toString()
    if(tempA.indexOf('.') != tempB.indexOf('.')) return false


    val aString = tempA.replace(".", "")
    val bString = tempB.replace(".", "")



    if(n > min(aString.length, bString.length)) return false
    val max = if(n <= min(aString.length, bString.length)) n else min(aString.length, bString.length)

    for (i in 0 until max) {
        if(aString[i] != bString[i]) return false
    }

    return true
}


fun createPointSet(center: Point, radius: Double, accuracy: Double) : List<Point> {

    val accuracyReach = if(accuracy > Circle.maxCircleAccuracy) Circle.maxCircleAccuracy else accuracy
    var currentAcc: Double
    var numberOfPoints = 2
    do {
        currentAcc = getAccuracy(++numberOfPoints, radius)
    } while (currentAcc < accuracyReach)

    val output = pointFactory(numberOfPoints, center, radius)

    return output
}

private fun pointFactory(
    numberOfPoints: Int,
    center: Point,
    radius: Double
): MutableList<Point> {
    var x: Double
    var y: Double
    var currentAngle = 0.0
    val angleChange = 2 * PI / numberOfPoints
    val output = mutableListOf<Point>()

    for (i in 0 until numberOfPoints) {
        x = center.x + cos(currentAngle) * radius
        y = center.y + sin(currentAngle) * radius
        val p = Point(x, y)
        output.add(p)
        currentAngle += angleChange
    }
    return output
}

fun main() {
    val digit6Accuracy = getAccuracy(9)


    val b = Circle(Point(0, 0), 10.0, digit6Accuracy)
    println(b)

    val c = Circle(Point(0, 0), 10.0, Circle.maxCircleAccuracy)
    println(c)
}