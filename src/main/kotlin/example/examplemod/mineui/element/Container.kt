package example.examplemod.mineui.element

import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStack
import example.examplemod.mineui.wrapper.translate
import net.minecraft.client.gui.Gui
import java.awt.Color

open class ContainerStyle : BoxStyle() {
    var scrollbar: ScrollbarStyle = ScrollbarStyle()
    var overflowX: Overflow = Overflow.Hidden
    var overflowY: Overflow = Overflow.Hidden

    fun overflow(overflow: Overflow) {
        overflowX = overflow
        overflowY = overflow
    }

    fun overflow(x: Overflow, y: Overflow) {
        overflowX = x
        overflowY = y
    }

    fun scrollbar(init: ScrollbarStyle.() -> Unit) {
        scrollbar = ScrollbarStyle().apply(init)
    }
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
    var width: Int = 10
    var thumb: Color = Color.BLUE
    var track: Color = Color.DARK_GRAY
}

abstract class Container<S: ContainerStyle>(create: () -> S) : BoxElement<S>(create) {
    private lateinit var minSize: Size
    override val children: ArrayList<UIElement<*>> = arrayListOf()
    var scrollX: Int = 0
    var scrollY: Int = 0

    override fun draw(stack: DrawStack, size: Size) {
        minSize = getMinimumSize()
        val cut = style.overflowX != Overflow.Visible || style.overflowY != Overflow.Visible
        var inner = size

        if (style.overflowX.isOverflowed(minSize, size)) {
            inner = Size(
                inner.width - style.scrollbar.width,
                inner.height
            )
        }

        if (style.overflowY.isOverflowed(minSize, size)) {
            inner = Size(inner.width, inner.height - style.scrollbar.width)
        }

        if (cut) {
            stack.scissor(0, 0, size.width, size.height)
        }

        super.draw(stack, inner)
    }

    override fun drawChildren(stack: DrawStack) {
        super.drawChildren(stack)
        Gui.disableScissor()

        stack.translate {
            stack.translated = absolutePosition
            val size = absoluteSize

            if (style.overflowX.isOverflowed(minSize, size)) {
                drawXScrollbar(stack, minSize, size)
            }

            if (style.overflowY.isOverflowed(minSize, size)) {
                drawYScrollbar(stack, minSize, size)
            }
        }
    }

    fun drawXScrollbar(stack: DrawStack, content: Size, size: Size) {
        val style = style.scrollbar
        val y = size.height - style.width
        stack.fillRect(0, y, size.width, style.width, style.track)

        val scale = (size.width.toDouble() / content.width ).coerceAtMost(1.0)
        stack.fillRect(scrollX, y, (size.width * scale).toInt(), style.width, style.thumb)
    }

    fun drawYScrollbar(stack: DrawStack, content: Size, size: Size) {
        val style = style.scrollbar
        val x = size.width - style.width
        stack.fillRect(x, 0, style.width, size.height, style.track)

        val scale = (size.height.toDouble() / content.height).coerceAtMost(1.0)
        stack.fillRect(x, scrollY, style.width, (size.height * scale).toInt(), style.thumb)
    }

    override fun invalidate() {
        children.clear()
    }
}