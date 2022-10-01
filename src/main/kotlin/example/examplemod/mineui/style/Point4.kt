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

    companion object {
        val Empty = Point4(0, 0, 0, 0)
    }
}

data class Point4F(
    val top: Float,
    val left: Float,
    val right: Float,
    val bottom: Float
) {
    val x: Float get() = left + right
    val y: Float get() = top + bottom

    companion object {
        val Empty = Point4F(0F, 0F, 0F, 0F)
    }
}

data class Border(
    val size: Point4,
    val color: Color
)