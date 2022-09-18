package example.examplemod.mineui.drawer

import example.examplemod.mineui.context.DrawContext
import example.examplemod.mineui.context.MouseContext
import example.examplemod.mineui.style.LabelStyle
import net.minecraft.network.chat.ComponentContents
import net.minecraft.network.chat.MutableComponent

class Label(
    text: String,
    val style: LabelStyle
) : Drawer() {
    private val comp = MutableComponent.create(ComponentContents.EMPTY).also {
        it.append(text)

        for (s in style.fontStyle)
            it.withStyle(s.style)
    }

    override fun getInfo(context: MouseContext) = Info(
        width = style.font.width(comp),
        height = style.font.lineHeight
    )

    override fun DrawContext.draw() {
        with (style) {
            font.draw(stack, comp, 0F, 0F, color.rgb)
        }
    }
}