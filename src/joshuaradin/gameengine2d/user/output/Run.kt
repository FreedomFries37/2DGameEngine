package joshuaradin.gameengine2d.user.output

import joshuaradin.gameengine2d.core.ProjectInfo

fun main() {
    run(ProjectInfo("Test"))
}

fun run(info: ProjectInfo){
    GameWindow(info.name)
}