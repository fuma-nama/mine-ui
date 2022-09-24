package example.examplemod.mineui.element.layout

import example.examplemod.mineui.element.Container
import example.examplemod.mineui.element.ContainerStyle
import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.utils.Size

class RowStyle : ContainerStyle() {
    var order: Order = Order.Normal
    var gap: Int = 0
}

class RowLayout : Container<RowStyle>(::RowStyle) {
    override fun getContentSize(): Size {
        var w = 0
        var h = 0
        for ((i, child) in children.withIndex()) {
            val size = child.getSize()

            if (i != 0) {
                w += style.gap
            }
            w += size.width
            h = size.height.coerceAtLeast(h)
        }

        return Size(w, h)
    }

    override fun reflowContent(pos: PosXY, padding: PosXY, size: Size) {
        val offset = pos + padding
        var left = 0
        val children = when (style.order) {
            Order.Normal -> children
            Order.Reversed -> children.reversed()
        }

        for (child in children) {
            val content = child.getSize()

            child.reflowNode(
                offset.add(x = left),
                Size(content.width, size.height)
            )

            left += content.width + style.gap
        }
    }
}