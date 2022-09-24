package example.examplemod.mineui.style

import example.examplemod.mineui.element.StyleContext

sealed interface PositionInput

fun StyleContext.relative() {
    position = Relative
}

object Relative : DynamicPosition {
    override fun receive(absolute: PosXY) = absolute
}

fun interface DynamicPosition: PositionInput {
    fun receive(absolute: PosXY): PosXY
}

data class PosXY(
    val x: Int, val y: Int
): PositionInput {
    fun add(x: Int = 0, y: Int = 0): PosXY {
        return PosXY(this.x + x, this.y + y)
    }

    operator fun plus(other: PosXY): PosXY {
        return PosXY(
            other.x + this.x,
            other.y + this.y
        )
    }
}