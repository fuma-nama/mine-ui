package example.examplemod.mineui

import com.mojang.blaze3d.vertex.PoseStack
import example.examplemod.mineui.core.Component
import example.examplemod.mineui.element.Container
import example.examplemod.mineui.element.StackLayout
import example.examplemod.mineui.element.UIElement
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStackDefault

data class PosXY(
    val x: Int, val y: Int
) {
    operator fun plus(other: PosXY): PosXY {
        return PosXY(
            other.x + this.x,
            other.y + this.y
        )
    }
}

class UI(var size: Size? = null, render: Component.() -> Unit) {
    val root = Component(this, render, element = StackLayout())

    init {
        root.ui = this
        root.render()
        reflow()
    }

    fun updateSize(w: Int, h: Int) {
        size = Size(w, h)
        reflow()
    }

    fun reflow() {
        val root = root.element
        root?.let {
            it.reflowNode(PosXY(0, 0), size?: it.getSize())
        }
    }

    fun draw(stack: PoseStack, mouseX: Int, mouseY: Int) {
        root.draw(DrawStackDefault(stack))
    }

    fun onClick(x: Double, y: Double, type: Int): Boolean {
        fireEvent(
            this.root.element!!,
            { element ->
                val pos = element.absolutePosition
                val size = element.absoluteSize

                pos.x <= x && pos.x + size.width >= x &&
                        pos.y <= y && pos.y + size.height >= y
            },
            { it.listener?.mouseClicked(x, y, type) == true }
        )
        return true
    }

    fun fireEvent(
        element: UIElement<*>,
        filter: (UIElement<*>) -> Boolean,
        action: (UIElement<*>) -> Boolean
    ) {
        if (filter(element) && action(element)) {
            return
        }

        if (element is Container<*>) {
            for (child in element.children.toList()) {
                fireEvent(child, filter, action)
            }
        }
    }

    fun onHover(x: Double, y: Double, type: Int): Boolean = false
}

data class HookKey(
    val type: String, val id: Any
)