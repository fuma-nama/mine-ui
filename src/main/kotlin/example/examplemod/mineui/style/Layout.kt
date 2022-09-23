package example.examplemod.mineui.style

enum class Align {
    Start, Center, End;

    fun getPosition(container: Int, content: Int): Int {
        return when (this) {
            Start -> 0
            Center -> (container / 2) - (content / 2)
            End -> container - content
        }.coerceAtLeast(0)
    }
}

enum class Direction {
    Column, Row
}