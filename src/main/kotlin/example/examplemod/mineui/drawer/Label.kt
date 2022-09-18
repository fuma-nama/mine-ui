package example.examplemod.mineui.drawer

import example.examplemod.mineui.context.DrawContext
import example.examplemod.mineui.context.MouseContext
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import java.awt.Color

class Label(
    val text: String,
    val font: Font = Minecraft.getInstance().font
) : Drawer() {
    override fun getInfo(context: MouseContext) = Info(
        width = font.width(text),
        height = font.wordWrapHeight(text, text.count { it == '\n' })
    )
    init {
        val a = font.wordWrapHeight("hello", 1)
        println("$a ${font.lineHeight}")
    }

    override fun DrawContext.draw() {
        font.draw(stack, text, 0F, 0F, Color.WHITE.rgb)
    }
}