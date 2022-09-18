package example.examplemod.mineui.drawer

import example.examplemod.mineui.context.DrawContext
import example.examplemod.mineui.context.MouseContext
import example.examplemod.mineui.style.ContainerStyle
import net.minecraft.client.gui.Gui

abstract class Container(val style: ContainerStyle) : Drawer() {
    override fun getInfo(context: MouseContext): Info {
        val content = getContentInfo(context)

        with (style) {
            return Info(
                width = content.width + padding.px,
                height = content.height + padding.py
            )
        }
    }

    override fun DrawContext.draw() {
        val info = getInfo(this)
        with (style) {
            if (background != null) {
                Gui.fill(stack, 0, 0, info.width, info.height, background!!.rgb)
            }

            stack.translate(padding.left.toDouble(), padding.top.toDouble(), 0.0)
        }

        drawChild()
    }

    abstract fun getContentInfo(context: MouseContext): Info
    abstract fun DrawContext.drawChild()
}