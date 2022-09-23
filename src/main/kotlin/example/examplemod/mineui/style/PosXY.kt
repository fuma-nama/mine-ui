package example.examplemod.mineui.style

data class PosXY(
    val x: Int, val y: Int
) {
    operator fun plus(other: PosXY): PosXY {
        return PosXY(
            other.x + this.x,
            other.y + this.y
        )
    }
}