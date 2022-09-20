package example.examplemod.mineui

import com.mojang.blaze3d.vertex.PoseStack
import example.examplemod.mineui.core.Component
import example.examplemod.mineui.element.StackLayout
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStackDefault

data class PosXY(
    val x: Int, val y: Int
)

class UI(val size: Size? = null, render: Component.() -> Unit) {
    val context = Component(this, render, element = StackLayout())

    init {
        context.ui = this
        context.render()
    }

    fun draw(stack: PoseStack, mouseX: Int, mouseY: Int) {
        context.draw(DrawStackDefault(stack), mouseX, mouseY, size)
    }

    fun onClick(x: Double, y: Double, type: Int): Boolean {
        println("$x, $y")
        return true
    }

    /*
    fun findFocus(mouseX: Double, mouseY: Double): Component? {
        fun isIn(data: LayoutData): Boolean {
            val (pos, size) = data

            return pos.x >= mouseX && pos.y <= mouseY &&
                    mouseX <= size.width && mouseY <= size.height
        }

        fun find(data: LayoutData): Component? {
            return if (isIn(data)) {
                for (child in data.comp.element.layout) {
                    val found = find(child)

                    if (found != null) {
                        return found
                    }
                }

                data.comp
            } else {
                null
            }
        }

        return find(LayoutData(
            PosXY(0, 0), context.element.getSize(), context
        ))
    }
     */

    fun onHover(x: Double, y: Double, type: Int): Boolean = false
}

data class HookKey(
    val type: String, val id: Any
)