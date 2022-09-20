package example.examplemod.mineui.element

import example.examplemod.mineui.PosXY
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.ComponentContents
import net.minecraft.network.chat.MutableComponent
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
    var text: String = ""

    private fun content() = MutableComponent.create(ComponentContents.EMPTY).also {
        it.append(text)
        it.style = style.textStyle
    }

    override fun getMinimumSize(): Size {
        return Size(
            width = style.font.width(content()),
            height = style.font.lineHeight
        )
    }

    override fun draw(stack: DrawStack, mouse: PosXY, size: Size) {
        with (style) {
            stack.drawText(font, content(), 0F, 0F, color)
        }
    }
}