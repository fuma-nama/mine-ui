package example.examplemod.mineui.style

import java.awt.Color

data class Point4(
    val top: Int,
    val left: Int,
    val right: Int,
    val bottom: Int
) {
    val px: Int get() = left + right
    val py: Int get() = top + bottom
}

interface PaddingBuilder {
    var padding: Point4

    fun padding(top: Int, left: Int, right: Int, bottom: Int) {
        padding = Point4(top, left, right, bottom)
    }

    fun padding(px: Int, py: Int) {
        padding = Point4(py, px, px, py)
    }

    fun padding(p: Int) {
        padding = Point4(p, p, p, p)
    }
}

open class ContainerStyle : PaddingBuilder {
    override var padding = Point4(0, 0, 0, 0)
    var gap: Int = 0
    var background: Color? = null
}