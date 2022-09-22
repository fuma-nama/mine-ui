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
    var direction: Direction = Direction.Column
}

enum class Direction {
    Column, Row
}

class StackLayout : Container<StackStyle>(::StackStyle) {
    fun contentSize(children: List<Size>): Size {
        var w = 0
        var h = 0

        for ((i, size) in children.withIndex()) {
            val gap = if (i != 0) style.gap else 0

            when (style.direction) {
                Direction.Row -> {
                    w += size.width + gap
                    h = size.height.coerceAtLeast(h)
                }
                Direction.Column -> {
                    w = size.width.coerceAtLeast(w)
                    h += size.height + gap
                }
            }
        }

        return Size(width = w, height = h)
    }

    override fun getContentSize(): Size {
        return contentSize(children.map { it.getSize() })
    }

    fun reflowRow(offset: PosXY, size: Size, sizes: List<Size>) {
        val content = contentSize(sizes)
        var left = 0
        val offsetX = style.align.getPosition(size.width, content.width)

        for ((i, child) in children.withIndex()) {
            val y = style.justify.getPosition(size.height, sizes[i].height)

            child.reflowNode(
                offset + PosXY(offsetX + left, y),
                sizes[i]
            )

            left += sizes[i].width + style.gap
        }
    }

    fun reflowColumn(offset: PosXY, size: Size, sizes: List<Size>) {
        val content = contentSize(sizes)
        var top = 0
        val offsetY = style.justify.getPosition(size.height, content.height)

        for ((i, child) in children.withIndex()) {
            val x = style.align.getPosition(size.width, sizes[i].width)

            child.reflowNode(
                offset + PosXY(x, offsetY + top),
                sizes[i]
            )

            top += sizes[i].height + style.gap
        }
    }

    override fun reflowContent(pos: PosXY, padding: PosXY, size: Size) {
        val sizes = children.map { it.getSize() }

        when (style.direction) {
            Direction.Row -> reflowRow(pos + padding, size, sizes)
            Direction.Column -> reflowColumn(pos + padding, size, sizes)
        }
    }
}