package example.examplemod.mineui.element.layout

import example.examplemod.mineui.element.Container
import example.examplemod.mineui.element.ContainerStyle
import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.utils.Size

enum class ItemHeight {
    Frame, Row, Self
}

class SimpleGridLayoutStyle : ContainerStyle() {
    var columns: IntArray = intArrayOf()
    var itemHeight: ItemHeight = ItemHeight.Row
    var spacing: PosXY = PosXY(0, 0)

    fun spacing(x: Int, y: Int = x) {
        spacing = PosXY(x, y)
    }

    fun spacingX(value: Int) {
        spacing = PosXY(value, spacing.y)
    }

    fun spacingY(value: Int) {
        spacing = PosXY(spacing.x, value)
    }

    fun columnsRepeat(repeat: Int) {
        //all equal to 1
        columns = IntArray(repeat) { 1 }
    }

    /**
     * @param sizes sizes of columns, final size: x * frame
     */
    fun columns(vararg columns: Int) {
        this.columns = columns
    }
}

class SimpleGridLayout : Container<SimpleGridLayoutStyle>(::SimpleGridLayoutStyle) {
    fun contentSize(children: List<Size>, frame: Size): Size {
        var w = 0
        var h = 0

        for ((i, row) in children.chunked(style.columns.size).withIndex()) {
            var rowW = 0
            var rowH = 0

            if (row.isNotEmpty()) {
                //add spacings
                rowW += (row.size - 1) * style.spacing.x
            }

            for ((j, col) in row.withIndex()) {
                rowW += style.columns[j] * frame.width
                rowH = col.height.coerceAtLeast(rowH)
            }

            if (i != 0) {
                h += style.spacing.y
            }
            w = rowW.coerceAtLeast(w)
            h += when (style.itemHeight) {
                ItemHeight.Frame -> frame.height
                else -> rowH
            }
        }
        return Size(w, h)
    }

    fun getFrameSize(sizes: List<Size>): Size {
        var w = 0
        var h = 0
        for (child in sizes) {
            w = child.width.coerceAtLeast(w)
            h = child.height.coerceAtLeast(h)
        }

        return Size(w, h)
    }

    override fun getContentSize(): Size {
        val sizes = children.map { it.getSize() }

        return contentSize(sizes, getFrameSize(sizes))
    }

    override fun reflowContent(pos: PosXY, padding: PosXY, size: Size) {
        val offset = pos + padding
        val scale = size.width / style.columns.sum()
        val sizes = children.map { it.getSize() }
        var top = 0
        var left = 0

        val frame by lazy {
            var w = 0
            var h = 0

            for (child in sizes) {
                w = child.width.coerceAtLeast(w)
                h = child.height.coerceAtLeast(h)
            }

            Size(w, h)
        }

        for ((i, row) in children.chunked(style.columns.size).withIndex()) {
            val rowH = run {
                var h = 0
                for (j in row.indices) {
                    val child = sizes[i + j]

                    h = child.height.coerceAtLeast(h)
                }

                h
            }

            //draw row
            for ((j, col) in row.withIndex()) {
                val height: Int = when (style.itemHeight) {
                    ItemHeight.Frame -> frame.height
                    ItemHeight.Row -> rowH
                    ItemHeight.Self -> sizes[i + j].height
                }
                val content = Size(scale * style.columns[j], height)

                if (j != 0) {
                    left += style.spacing.x
                }
                col.reflowNode(offset + PosXY(left, top), content)

                left += content.width
            }

            if (i != 0) {
                top += style.spacing.y
            }

            top += when (style.itemHeight) {
                ItemHeight.Frame -> frame.height
                else -> rowH
            }
        }
    }
}