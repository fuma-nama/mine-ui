package example.examplemod.mineui.element

import example.examplemod.mineui.PosXY
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStack

enum class Align {
    Start, Center, End
}

class StackStyle : ContainerStyle() {
    var align: Align = Align.Start
    var justify: Align = Align.Start
}

class StackLayout : Container<StackStyle>(::StackStyle) {
    override fun getContentSize(): Size {
        var w = 0
        var maxH = 0

        for ((i, child) in children.withIndex()) {
            if (i != 0) {
                w += style.gap
            }

            val size = child.actualSize
            w += size.width
            maxH = maxH.coerceAtLeast(size.height)
        }

        return Size(width = w, height = maxH)
    }

    override fun drawContent(stack: DrawStack, mouse: PosXY, size: Size) {
        val x = when (style.align) {
            Align.Start -> 0
            Align.Center -> (size.width / 2) - (minSize.width / 2)
            Align.End -> size.width - minSize.width
        }

        val y = when (style.justify) {
            Align.Start -> 0
            Align.Center -> (size.height / 2) - (minSize.height / 2)
            Align.End -> size.height - minSize.height
        }

        stack.translate(x.coerceAtLeast(0), y.coerceAtLeast(0))

        for (child in children) {
            child.draw(stack, mouse)
            val add = child.actualSize.width + style.gap

            stack.translate(add)
        }
    }
}