package example.examplemod.mineui.element.input

import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.font.TextFieldHelper
import net.minecraft.network.chat.Component
import java.awt.Color

class TextFieldStyle : TextInputStyle() {
    init {
        color = Color.WHITE
        background = Color.BLACK
        border(1, 1, Color.DARK_GRAY)
        padding(4)
    }
}

class TextFieldElement : TextInput<TextFieldStyle>(::TextFieldStyle) {
    private val splitter get() = style.font.splitter
    val value get() = style.value
    var offsetX: Int = 0
    override val helper = FieldHelperImpl(
        { value },
        { style.onChange(it) }
    )

    fun asComponent(value: String = this.value) = Component.literal(value).setStyle(style.textStyle)

    override fun positionToIndex(mouseX: Double, mouseY: Double): Int {
        val ox = mouseX - (contentOffset.x + absolutePosition.x) - offsetX

        return splitter.plainIndexAtWidth(value, ox.toInt(), style.textStyle).coerceIn(0, value.length)
    }

    override fun drawInput(stack: DrawStack, size: Size, focus: Boolean) {
        val cursorSpace = style.cursor.width + 5
        val cursorPos = indexToPosition(cursor).x //ignore y

        if (value.isNotEmpty()) {
            if (cursorPos + cursorSpace > size.width) {
                this.offsetX = - (cursorPos + cursorSpace - size.width)
                stack.translate(x = offsetX)
            }

            stack.drawText(style.font, asComponent(), 0F, 0F, style.color)

            if (helper.isSelecting) {
                val selectionPos = indexToPosition(selection).x
                val left = cursorPos.coerceAtMost(selectionPos)
                val right = cursorPos.coerceAtLeast(selectionPos)

                stack.drawHighlight(left, 0, right - left, style.font.lineHeight)
            }
        } else {
            stack.drawText(style.font, style.placeholder, 0F, 0F, style.placeholderColor)
        }

        if (tick / style.cursor.step % 2 == 0 && focus) {
            stack.fillRect(cursorPos, 0, style.cursor.width, style.font.lineHeight, style.cursor.color)
        }
    }

    override fun indexToPosition(index: Int): PosXY {
        return PosXY(
            style.font.width(asComponent(
                value.substring(0, index.coerceIn(0, value.length))
            )),
            0
        )
    }
}

class FieldHelperImpl(v: () -> String, setV: (String) -> Unit, validator: (String) -> Boolean = { true }) : TextFieldHelper(
    v,
    setV,
    createClipboardGetter(Minecraft.getInstance()),
    createClipboardSetter(Minecraft.getInstance()),
    validator
) {
    var tick: Int = 0
    var lastCursor: Int? = null

    fun updateTick() {
        tick = if (cursorPos != lastCursor || tick == Int.MAX_VALUE) {
            lastCursor = cursorPos
            0
        } else {
            tick + 1
        }
    }
}