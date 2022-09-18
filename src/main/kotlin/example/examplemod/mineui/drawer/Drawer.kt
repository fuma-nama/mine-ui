package example.examplemod.mineui.drawer

import example.examplemod.mineui.RenderContext
import example.examplemod.mineui.context.DrawContext
import example.examplemod.mineui.context.MouseContext

data class Info(val width: Int, val height: Int)
abstract class Drawer {
    open lateinit var rendered: RenderContext
    abstract fun getInfo(context: MouseContext): Info
    abstract fun DrawContext.draw()

    fun isFocus(context: DrawContext): Boolean {
        val info = getInfo(context)

        if (context.mouseX < info.width) {

        }

        return false
    }

    fun onClick(x: Double, y: Double, type: Int): Boolean = false
    fun onHover(x: Double, y: Double, type: Int): Boolean = false
}

open class DefaultDrawer(context: RenderContext): Drawer() {
    override var rendered = context

    override fun getInfo(context: MouseContext): Info {
        return Info(0, 0)
    }

    override fun DrawContext.draw() {
        for (child in rendered.children.values) {
            child.draw()
        }
    }
}