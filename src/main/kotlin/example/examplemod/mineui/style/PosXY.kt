package example.examplemod.mineui.style

data class PosXY(
    val x: Int, val y: Int
) {
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