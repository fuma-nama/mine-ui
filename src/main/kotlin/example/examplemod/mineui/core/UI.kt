package example.examplemod.mineui.core

import com.mojang.blaze3d.vertex.PoseStack
import example.examplemod.mineui.element.Container
import example.examplemod.mineui.element.UIElement
import example.examplemod.mineui.element.layout.StackLayout
import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStackDefault
import net.minecraft.client.gui.Gui

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
        //clear states after drawing ui elements
        Gui.disableScissor()
    }

    fun onClick(x: Double, y: Double, type: Int): Boolean {
        fireEvent(
            this.root.element!!,
            { element -> element.isIn(x, y) },
            { element, context ->
                element.listener?.onClick(x, y, type, context)
            }
        )
        return true
    }

    fun onDrag(
        mouseX: Double,
        mouseY: Double,
        mouseButton: Int,
        dragX: Double,
        dragY: Double
    ): Boolean {

        fireEvent(this.root.element!!,
            { element -> element.isIn(mouseX, mouseY) },
            { element, context ->
                element.listener?.onDrag(mouseX, mouseY, mouseButton, dragX, dragY, context)
            }
        )
        return true
    }

    fun fireEvent(
        element: UIElement<*>,
        filter: (UIElement<*>) -> Boolean,
        action: (UIElement<*>, GuiEventContext) -> Unit
    ) {
        if (filter(element)) {
            val context = GuiEventContext(this, element)
            action(element, context)

            if (context.reflow) reflow()
            if (context.prevent) return
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