package example.examplemod.mineui.element

import example.examplemod.mineui.PosXY
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStack
import java.awt.Color

interface BoxBuilder {
    var padding: Point4
    var background: Color?

    fun padding(top: Int, left: Int, right: Int, bottom: Int) {
        padding = Point4(top, left, right, bottom)
    }

    fun padding(px: Int, py: Int) {
        padding = Point4(py, px, px, py)
    }

    fun padding(p: Int) {
        padding = Point4(p, p, p, p)
    }
}

open class BoxStyle: StyleContext(), BoxBuilder {
    override var padding = Point4(0, 0, 0, 0)
    override var background: Color? = null
}

abstract class BoxElement<S: BoxStyle>(create: () -> S): UIElement<S>(create) {
    abstract fun getContentSize(): Size

    override fun getMinimumSize() = getContentSize()
    override fun getMaximumSize(): Size {
        return Size(
            width = size.width + style.padding.px,
            height = size.height + style.padding.py
        )
    }

    override fun draw(stack: DrawStack, mouse: PosXY, size: Size) {
        val before = stack.translated
        with (style) {
            if (background != null) {
                stack.fillRect(0, 0, size.width, size.height, background!!)
            }

            stack.translate(padding.left, padding.top)
        }

        drawContent(stack, mouse, Size(
            size.width - style.padding.px,
            size.height - style.padding.py
        ))

        stack.translated = before
    }

    abstract fun drawContent(stack: DrawStack, mouse: PosXY, size: Size)
}