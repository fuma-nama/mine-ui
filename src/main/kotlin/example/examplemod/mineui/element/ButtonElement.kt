package example.examplemod.mineui.element

import example.examplemod.mineui.style.Point4
import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import java.awt.Color

class ButtonStyle : BoxStyle(), LabelBuilder {
    override var textStyle = Style.EMPTY
    override var color: Color = Color.WHITE
    override var font: Font = Minecraft.getInstance().font
    override var background: Color? = Color.DARK_GRAY
    override var padding = Point4(5, 5, 5, 5)
}

class ButtonElement : BoxElement<ButtonStyle>(::ButtonStyle) {
    val content = Component.empty()

    override fun reflowContent(pos: PosXY, padding: PosXY, size: Size) {
        super.reflowContent(pos, padding, size)
        println("reflow")
    }
    fun setText(s: String) {
        content.siblings.clear()
        content.append(s)
        content.style = style.textStyle
    }

    override fun getContentSize(): Size {
        return Size(
            width = style.font.width(content),
            height = style.font.lineHeight
        )
    }

    override fun drawContent(stack: DrawStack, size: Size) {
        stack.drawText(style.font, content, 0F, 0F, style.color)
    }
}