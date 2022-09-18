package example.examplemod.mineui

import com.mojang.blaze3d.vertex.PoseStack

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