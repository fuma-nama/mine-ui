package example.examplemod.mineui.style

import example.examplemod.mineui.utils.Size
import java.awt.Color

enum class VerticalAlign {
    Start, Center, End;

    fun getPosition(container: Size, content: Size) = getPosition(container.height, content.height)

    fun getPosition(container: Int, content: Int): Int {
        return when (this) {
            Start -> 0
            Center -> (container / 2) - (content / 2)
            End -> container - content
        }.coerceAtLeast(0)
    }
}

enum class HorizontalAlign {
    Start, Center, End, Fill;

    fun getPosition(container: Size, content: Size) = getPosition(container.width, content.width)

    fun getPosition(container: Int, content: Int): Int {
        return when (this) {
            Center -> (container / 2) - (content / 2)
            End -> container - content
            else -> 0
        }.coerceAtLeast(0)
    }

    fun getSize(container: Size, element: Size): Size {

        return when (this) {
            Fill -> Size(container.width, element.height)
            else -> element
        }
    }
}

enum class Direction {
    Column, Row
}

enum class Overflow {
    Auto, Scroll, Visible, Hidden;

    fun isOverflowed(min: Size, size: Size): Boolean {
        return when (this) {
            Scroll -> true
            Hidden, Visible -> false
            Auto -> min.width > size.width
        }
    }
}

class ScrollbarStyle {
    var width: Int = 5
    var thumb: Color = Color.LIGHT_GRAY
    var track: Color = Color.DARK_GRAY
    var speed: Double = 3.0
}