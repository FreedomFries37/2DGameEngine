package joshuaradin.gameengine2d.standard.type

import kotlin.math.*

class Triangle(p1: Point, p2: Point, p3: Point) : Shape(p1, p2 , p3)
class Quadrilateral(p1: Point, p2: Point, p3: Point, p4: Point) : Shape(p1, p2, p3, p4)




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

    var upperBound = numPoints
    var lowerBound = numPoints/2


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