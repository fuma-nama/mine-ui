package example.examplemod.mineui.element

import example.examplemod.mineui.style.Point4
import example.examplemod.mineui.style.PosXY
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

    override fun reflow(pos: PosXY, size: Size) {
        val padding = style.padding

        val inner = Size(
            width = size.width - padding.px,
            height = size.height - padding.py
        )

        reflowContent(pos, PosXY(padding.left, padding.right), inner)
    }

    /**
     * @param size Size of content
     */
    open fun reflowContent(pos: PosXY, padding: PosXY, size: Size) = Unit

    override fun getMinimumSize(): Size {
        val content = getContentSize()

        return Size(
            width = content.width + style.padding.px,
            height = content.height + style.padding.py
        )
    }

    override fun draw(stack: DrawStack, size: Size) {
        with (style) {
            if (background != null) {
                stack.fillRect(0, 0, size.width, size.height, background!!)
            }
            stack.translate(padding.left, padding.right)
        }

        drawContent(stack, Size(
            size.width - style.padding.px,
            size.height - style.padding.py
        ))
    }

    open fun drawContent(stack: DrawStack, size: Size) = Unit
}