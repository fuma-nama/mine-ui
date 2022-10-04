package example.examplemod.mineui.element.input

import example.examplemod.mineui.core.GuiEventContext
import example.examplemod.mineui.element.StyleContext
import example.examplemod.mineui.element.UIElement
import example.examplemod.mineui.style.Border
import example.examplemod.mineui.style.Point4
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStack
import java.awt.Color

class RadioButtonStyle: StyleContext() {
    var border: Border? = Border(Point4(2, 2, 2, 2), Color.WHITE)
    var checked: Color? = Color.WHITE
    var unchecked: Color? = null
    var padding: Point4 = Point4(1, 1, 1, 1)

    var value: Boolean = false
    var onChange: (Boolean) -> Unit = {}
    init {
        requireFocus = true
    }
}

class RadioButtonElement : UIElement<RadioButtonStyle>(::RadioButtonStyle) {
    override fun onClick(x: Double, y: Double, mouseButton: Int, context: GuiEventContext) {
        style.onChange(!style.value)
        context.requireReflow()

        super.onClick(x, y, mouseButton, context)
    }

    override fun draw(stack: DrawStack, size: Size) {
        var radioSize = size
        val border = style.border

        if (border != null) {
            stack.drawBorder(0, 0, size.width, size.height, border.color, border.size)
            stack.translate(border.size.left, border.size.top)

            radioSize -= border.size.toSize()
        }

        stack.translate(style.padding.left, style.padding.top)
        radioSize -= style.padding.toSize()

        val color = if (style.value) style.checked else style.unchecked

        if (color != null) {
            stack.fillRect(0, 0, radioSize.width, radioSize.height, color )
        }
    }

    override fun getMinimumSize(): Size {
        val border = style.border?.size

        return Size(15, 15).plus(
            width = border?.px?: 0,
            height = border?.py?: 0
        )
    }
}