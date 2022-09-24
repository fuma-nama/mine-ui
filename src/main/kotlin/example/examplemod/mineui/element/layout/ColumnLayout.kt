package example.examplemod.mineui.element.layout

import example.examplemod.mineui.element.Container
import example.examplemod.mineui.element.ContainerStyle
import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.utils.Size

enum class Order {
    Normal, Reversed
}

/**
 * similar to a StackLayout in 'columns' direction
 *
 * Children width are automatically fit to its parent
 */
class ColumnStyle: ContainerStyle() {
    var gap: Int = 0
    var order: Order = Order.Normal
}

class ColumnLayout : Container<ColumnStyle>(::ColumnStyle) {
    override fun getContentSize(): Size {
        var w = 0
        var h = 0
        for ((i, child) in children.withIndex()) {
            val size = child.getSize()

            if (i != 0) {
                h += style.gap
            }
            w = size.width.coerceAtLeast(w)
            h += size.height
        }

        return Size(w, h)
    }

    override fun reflowContent(pos: PosXY, padding: PosXY, size: Size) {
        val offset = pos + padding
        var top = 0
        val children = when (style.order) {
            Order.Normal -> children
            Order.Reversed -> children.reversed()
        }

        for (child in children) {
            val content = child.getSize()

            child.reflowNode(
                offset.add(y = top),
                Size(size.width, content.height)
            )

            top += content.height + style.gap
        }
    }
}