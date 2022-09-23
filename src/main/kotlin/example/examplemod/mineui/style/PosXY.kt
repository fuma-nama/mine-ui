package example.examplemod.mineui.style

data class PosXY(
    val x: Int, val y: Int
) {
    fun addX(x: Int): PosXY {
        return PosXY(x + this.x, y)
    }

    operator fun plus(other: PosXY): PosXY {
        return PosXY(
            other.x + this.x,
            other.y + this.y
        )
    }
}