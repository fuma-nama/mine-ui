package example.examplemod.mineui.element.internal

import example.examplemod.mineui.core.EventType
import example.examplemod.mineui.core.GuiEventContext
import example.examplemod.mineui.element.BoxElement
import example.examplemod.mineui.element.BoxStyle
import example.examplemod.mineui.style.Overflow
import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.style.ScrollbarStyle
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStack
import example.examplemod.mineui.wrapper.lockState
import net.minecraft.client.gui.Gui

open class ScrollViewStyle : BoxStyle() {
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

enum class Scrolling {
    X, Y
}

abstract class ScrollView<S: ScrollViewStyle>(create: () -> S) : BoxElement<S>(create) {
    private var scrolling: Scrolling? = null
    private val cutOverflow get() =
        style.overflowX != Overflow.Visible || style.overflowY != Overflow.Visible

    override fun onClick(x: Double, y: Double, mouseButton: Int, context: GuiEventContext) {
        super.onClick(x, y, mouseButton, context)
        if (scrolling != null) return
        val hooked = ui.state.hooked

        val top = absolutePosition.y + absoluteSize.height - style.scrollbar.width
        val bottom = absolutePosition.y + absoluteSize.height
        val left = absolutePosition.x + absoluteSize.width - style.scrollbar.width
        val right = absolutePosition.x + absoluteSize.width

        scrolling = when {
            overflowX && y.toInt() in top..bottom -> Scrolling.X
            overflowY && x.toInt() in left..right -> Scrolling.Y
            else -> null
        }
        if (scrolling != null) {
            hooked[EventType.MouseDrag] = this
            hooked[EventType.MouseRelease] = this
        }
    }

    override fun onDrag(
        mouseX: Double,
        mouseY: Double,
        mouseButton: Int,
        dragX: Double,
        dragY: Double,
        context: GuiEventContext,
    ) {
        when (scrolling) {
            Scrolling.X -> {
                context.reflow = true
                scrollX = (scrollX + dragX.toInt())
                    .coerceAtMost(minSize.width - absoluteSize.width)
                    .coerceAtLeast(0)
            }
            Scrolling.Y -> {
                context.reflow = true
                scrollY = (scrollY + dragY.toInt())
                    .coerceAtMost(minSize.height - absoluteSize.height)
                    .coerceAtLeast(0)
            }
            else -> {}
        }

        return super.onDrag(mouseX, mouseY, mouseButton, dragX, dragY, context)
    }

    override fun onScroll(x: Double, y: Double, scroll: Double, context: GuiEventContext) {
        if (overflowY) {
            context.reflow = true
            context.prevent = true
            scrollY = (scrollY - (scroll * style.scrollbar.speed)).toInt()
                .coerceAtMost(minSize.height - absoluteSize.height)
                .coerceAtLeast(0)
        }

        super.onScroll(x, y, scroll, context)
    }

    override fun onMouseReleased(x: Double, y: Double, mouseButton: Int, context: GuiEventContext) {
        val hooked = ui.state.hooked

        scrolling = null
        hooked.remove(EventType.MouseDrag, this)
        hooked.remove(EventType.MouseRelease, this)
        super.onMouseReleased(x, y, mouseButton, context)
    }

    lateinit var minSize: Size
    var scrollX: Int = 0
    var scrollY: Int = 0
    var overflowX: Boolean = false
    var overflowY: Boolean = false

    override fun reflow(pos: PosXY, size: Size) {
        val min = getMinimumSize()
        overflowX = style.overflowX.isOverflowed(min, size)
        overflowY = style.overflowY.isOverflowed(min, size)
        minSize = min.plus(
            width =  if (overflowY) style.scrollbar.width else 0,
            height = if (overflowX) style.scrollbar.width else 0
        )

        super.reflow(pos.minus(x = scrollX, y = scrollY), size.minusScrollbars())
    }

    override fun drawChildren(stack: DrawStack) {
        stack.translated = absolutePosition
        val size = absoluteSize

        for (element in children) {
            if (cutOverflow) {
                //reset scissor in every render
                stack.scissor(0, 0, size.width, size.height)
            }

            stack.lockState {
                element.drawNode(stack)
            }
        }

        Gui.disableScissor()

        if (overflowX) {
            drawXScrollbar(stack, minSize, size)
        }

        if (overflowY) {
            drawYScrollbar(stack, minSize, size)
        }
    }

    fun drawXScrollbar(stack: DrawStack, content: Size, size: Size) {
        val style = style.scrollbar
        val y = size.height - style.width
        stack.fillRect(0, y, size.width, style.width, style.track)

        val bar = (size.width * size.width) / content.width
        val extraWidth = content.width - size.width
        val left = scrollX * (size.width - bar) / extraWidth

        stack.fillRect(left, y, bar, style.width, style.thumb)
    }

    fun drawYScrollbar(stack: DrawStack, content: Size, size: Size) {
        val style = style.scrollbar
        val x = size.width - style.width
        stack.fillRect(x, 0, style.width, size.height, style.track)


        val bar = (size.height * size.height) / content.height
        val extraHeight = content.height - size.height
        val top = scrollY * (size.height - bar) / extraHeight

        stack.fillRect(x, top, style.width, bar, style.thumb)
    }

    private fun Size.minusScrollbars(): Size {
        return minus(
            width =  if (overflowY) style.scrollbar.width else 0,
            height = if (overflowX) style.scrollbar.width else 0
        )
    }
}