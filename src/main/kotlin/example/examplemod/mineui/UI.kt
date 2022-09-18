package example.examplemod.mineui

import com.mojang.blaze3d.vertex.PoseStack
import example.examplemod.mineui.context.RenderContext

class UI(render: RenderContext.() -> Unit) {
    val context = RenderContext(this, render)

    init {
        context.ui = this
        context.render()
    }

    fun draw(stack: PoseStack, mouseX: Int, mouseY: Int) {
        context.draw(stack, mouseX, mouseY)
    }

    fun onClick(x: Double, y: Double, type: Int): Boolean {
        return context.drawer.onClick(x, y, type)
    }

    fun onHover(x: Double, y: Double, type: Int): Boolean {
        return context.drawer.onHover(x, y, type)
    }
}

data class HookKey(
    val type: String, val id: Any
)