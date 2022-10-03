package example.examplemod.mineui.element

import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStack
import example.examplemod.mineui.wrapper.drawTextLines
import example.examplemod.mineui.wrapper.minSize
import example.examplemod.mineui.wrapper.splitLines
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import java.awt.Color

interface LabelBuilder {
    var textStyle: Style
    val color: Color
    val font: Font

    fun bold() {
        textStyle = textStyle.withBold(true)
    }

    fun underline() {
        textStyle = textStyle.withUnderlined(true)
    }

    fun italic() {
        textStyle = textStyle.withItalic(true)
    }

    fun obfuscated() {
        textStyle = textStyle.withObfuscated(true)
    }
}

class LabelStyle : StyleContext(), LabelBuilder {
    override var textStyle = Style.EMPTY

    override var color: Color = Color.WHITE
    override var font: Font = Minecraft.getInstance().font
}

class TextElement : UIElement<LabelStyle>(::LabelStyle) {
    lateinit var content: List<Component>
    var text: String = ""

    override fun reflow(pos: PosXY, size: Size) {
        super.reflow(pos, size)
        content = style.font.splitLines(text, style.textStyle, size.width)
    }

    override fun getMinimumSize(): Size {
        return style.font.minSize(text, style.textStyle)
    }

    override fun draw(stack: DrawStack, size: Size) {
        with (style) {
            stack.drawTextLines(0F, 0F, font, content, color)
        }
    }
}