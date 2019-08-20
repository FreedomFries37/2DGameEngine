package joshuaradin.gameengine2d.engine.core.basic

import joshuaradin.gameengine2d.user.output.Renderer2DComponent

class GameObjectInfo (val gameObject: GameObject) : Iterable<ComponentInfo<*>> {

    private val components: List<ComponentInfo<*>>

    init {
        components = List(gameObject.numComponents) {
            ComponentInfo(
                gameObject.getComponentReference(
                    it
                ).comp
            )
        }
    }


    fun printInfo() {
        if (gameObject.enabled) {
            val hasComponent = gameObject.hasComponent<Renderer2DComponent>()
            if (gameObject.started && hasComponent) {
                print("[#] ")
            } else if (hasComponent) {
                print("[-] ")
            } else print("[ ] ")
        } else print("    ")

        println(gameObject.directedName())
        for (component in components) {
            println("\t     " + component.toString().replace("\n", "\n\t    \t"))
        }
    }

    fun GameObject.directedName() : String {
        return (if(parent != null) parent!!.directedName() + "/" else "") + this.toString()
    }

    /**
     * Returns an iterator over the elements of this object.
     */
    override fun iterator(): Iterator<ComponentInfo<*>> {
        return components.iterator()
    }
}