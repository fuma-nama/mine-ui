package example.examplemod.mineui.style

data class Point4(
    val top: Int,
    val left: Int,
    val right: Int,
    val bottom: Int
) {
    val px: Int get() = left + right
    val py: Int get() = top + bottom
}