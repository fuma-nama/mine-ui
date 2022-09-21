package example.examplemod.mineui.element

import example.examplemod.mineui.PosXY
import example.examplemod.mineui.utils.Size

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

            val size = child.getSize()

            w += size.width
            maxH = size.height.coerceAtLeast(maxH)
        }

        return Size(width = w, height = maxH)
    }

    override fun reflowContent(pos: PosXY, padding: PosXY, size: Size) {
        val offset = pos + padding
        val content = getContentSize()

        var left = 0

        println("container ${size.width} content ${content.width}")
        val x = style.align.getPosition(size.width, content.width)
        val y = style.justify.getPosition(size.height, content.height)

        for (child in children) {
            val childSize = child.getSize()

            child.reflowNode(
                offset + PosXY(x + left, y),
                childSize
            )

            left += childSize.width + style.gap
        }
    }
}
/*
content size 56
content size 66
content size 52
content size 58
content size 412
container 417 content 468
content size 66
content size 52
content size 58
container 392 content 186
draw 417
draw 56
draw 412
 */