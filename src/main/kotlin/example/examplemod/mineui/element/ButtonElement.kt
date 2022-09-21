package example.examplemod.mineui.element

import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.events.GuiEventListener
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
    init {
        listener = object : GuiEventListener {
            override fun mouseClicked(p_94737_: Double, p_94738_: Double, p_94739_: Int): Boolean {
                println("clicked")
                return super.mouseClicked(p_94737_, p_94738_, p_94739_)
            }
        }
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