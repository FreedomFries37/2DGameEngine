package joshuaradin.gameengine2d.test


import javafx.scene.input.KeyCode
import joshuaradin.gameengine2d.core.basic.GameObject
import joshuaradin.gameengine2d.core.basic.ObjectBehavior
import joshuaradin.gameengine2d.core.service.GameObjectTracker
import joshuaradin.gameengine2d.standard.component.InterferenceBoundary
import joshuaradin.gameengine2d.standard.component.rendering.LineRenderer
import joshuaradin.gameengine2d.standard.component.rendering.ShapeRenderer
import joshuaradin.gameengine2d.standard.type.Vector2
import joshuaradin.gameengine2d.standard.type.geometry.Line
import joshuaradin.gameengine2d.standard.type.geometry.Point
import joshuaradin.gameengine2d.standard.type.geometry.Rotation
import joshuaradin.gameengine2d.standard.type.geometry.Square
import joshuaradin.gameengine2d.user.connecting.Screen
import joshuaradin.gameengine2d.user.connecting.Time
import joshuaradin.gameengine2d.user.input.Input

class TestScript : ObjectBehavior() {

    var power = 100.0
    var line: Line? = null
    var shapeRenderer: ShapeRenderer? = null
    var cameraObject: GameObject? = null

    var shrink = true

    override fun start() {
        shapeRenderer = addComponent()
        val interferenceBoundary = addComponent<InterferenceBoundary>()
        val square = Square(Point.ZERO, 40.0)

        interferenceBoundary?.boundary = square
        val lineRenderer = addComponent<LineRenderer>()

        val quadrilateral = square

        shapeRenderer?.shape = quadrilateral

        lineRenderer?.line = Line()

        line = lineRenderer?.line!!
        lineRenderer.level = 100

        cameraObject = GameObjectTracker.instance.find("Camera")

        val center = gameObject?.instantiate(Center.gameObject)
        center?.removeParent()
        transform?.position = Vector2(10, 10)
        Screen.camera()?.transform?.rotation= Rotation.createDeg(45.0)
    }

    override fun update() {

        if(shrink){
            val vector2 = Vector2(.1, .1) * 10.0 * Time.deltaTime
            transform!!.scale -= vector2
        }else{
            val vector2 = Vector2(.1, .1) * 10.0 *Time.deltaTime
            transform!!.scale += vector2
        }

        if(shrink && transform!!.scale.x <= 0.1) {
            shrink = false
        }else if(!shrink && transform!!.scale.x >= 1){
            shrink = true
        }

        transform!!.rotation += Rotation.createDeg(15.0 * Time.deltaTime)


        if (Input.getKey(KeyCode.W)) {
            line!!.v += Vector2.distanceWithRot(power, Rotation.createDeg(-90.0))* Time.deltaTime

        }

        if (Input.getKey(KeyCode.A)) {
            line!!.v += Vector2.distanceWithRot(power, Rotation.createDeg(180.0))* Time.deltaTime

        }
        if (Input.getKey(KeyCode.S)) {
            line!!.v += Vector2.distanceWithRot(power, Rotation.createDeg(90.0))* Time.deltaTime

        }
        if (Input.getKey(KeyCode.D)) {
            line!!.v += Vector2.distanceWithRot(power, Rotation.createDeg(0.0))* Time.deltaTime

        }

        if (Input.getKey(KeyCode.UP)) {
            transform!!.position += Vector2.distanceWithRot(power, Rotation.createDeg(-90.0))* Time.deltaTime
        }

        if (Input.getKey(KeyCode.LEFT)) {
            transform!!.position += Vector2.distanceWithRot(power, Rotation.createDeg(180.0))* Time.deltaTime

        }
        if (Input.getKey(KeyCode.DOWN)) {
            transform!!.position += Vector2.distanceWithRot(power, Rotation.createDeg(90.0))* Time.deltaTime

        }
        if (Input.getKey(KeyCode.RIGHT)) {
            transform!!.position += Vector2.distanceWithRot(power, Rotation.createDeg(0.0))* Time.deltaTime

        }

        line!!.u = transform!!.position.toPoint()

        if (Input.getKeyDown(KeyCode.F)) {
            GameObjectTracker.instance.printAllObjectSceneAndPosition()
        }
        if (Input.getKeyDown(KeyCode.G)) {
            GameObjectTracker.instance.printFullInfo()
        }

        if(cameraObject != null && line != null){
            //gameObject?.transform?.position = line!!.v.asVector2()
            //cameraObject!!.transform.position = line!!.v.asVector2()
            shapeRenderer!!.shape = shapeRenderer!!.shape?.recenter( line!!.u.asVector2())
            //shapeRenderer!!.shape?.printInfo()
        }


    }

    override fun onMouseClick() {
        println("Yahaha you found me")
    }
}