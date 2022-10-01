package example.examplemod.mineui.core

import com.mojang.blaze3d.vertex.PoseStack
import example.examplemod.mineui.element.Container
import example.examplemod.mineui.element.UIElement
import example.examplemod.mineui.element.layout.StackLayout
import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStackDefault
import example.examplemod.mineui.wrapper.GUIListener
import net.minecraft.client.gui.Gui

class UI(var size: Size? = null, render: Component.() -> Unit) {
    val state = UIState()
    val root = Component(this, render, element = StackLayout())

    init {
        root.ui = this
        root.render()
        reflow()
    }

    fun focus(element: UIElement<*>) {
        this.state.focus = element
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
        return fireEvent(
            EventType.Click,
            { element -> element.isIn(x, y) },
            { element, context ->
                element.onClick(x, y, type, context)
            }
        )
    }

    fun onType(char: Char, key: Int): Boolean {
        executeFocus { element, context ->
            element.onType(char, key, context)
        }

        return true
    }

    fun onMouseReleased(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        return fireEvent(
            EventType.MouseRelease,
            { element -> element.isIn(mouseX, mouseY) },
            { element, context ->
                element.onMouseReleased(mouseX, mouseY, mouseButton, context)
            }
        )
    }

    fun onScroll(mouseX: Double, mouseY: Double, scroll: Double): Boolean {
        return fireEvent(
            EventType.MouseScroll,
            { element -> element.isIn(mouseX, mouseY) },
            { element, context ->
                element.onScroll(mouseX, mouseY, scroll, context)
            }
        )
    }

    fun onDrag(
        mouseX: Double,
        mouseY: Double,
        mouseButton: Int,
        dragX: Double,
        dragY: Double
    ): Boolean {

        return fireEvent(
            EventType.MouseDrag,
            { element -> element.isIn(mouseX, mouseY) },
            { element, context ->
                element.onDrag(mouseX, mouseY, mouseButton, dragX, dragY, context)
            }
        )
    }

    fun onKeyPress(key: Int, o: Int, y: Int): Boolean {
        return executeFocus { element, context ->
            element.onKeyPressed(key, o, y, context)
        }
    }

    fun executeFocus(action: (GUIListener, GuiEventContext) -> Unit): Boolean {
        return state.focus?.let {
            return execute(it, action).prevent
        }?: false
    }

    fun execute(element: UIElement<*>, action: (GUIListener, GuiEventContext) -> Unit): GuiEventContext {
        val context = GuiEventContext(this, element)
        action(element, context)

        return context
    }

    fun fireEvent(
        type: EventType,
        filter: (UIElement<*>) -> Boolean,
        action: (GUIListener, GuiEventContext) -> Unit
    ): Boolean {
        var reflow = false

        fun execute(element: UIElement<*>): GuiEventContext {
            val context = execute(element, action)

            if (!reflow) {
                reflow = context.reflow
            }
            return context
        }

        /**
         * Returns True if event is prevented
         */
        fun next(element: UIElement<*>): Boolean {
            if (element is Container<*>) {
                for (child in element.children.toList()) {
                    if (next(child)) return true
                }
            }

            if (filter(element)) {
                val executed = execute(element)

                if (executed.prevent) return true
            }

            return false
        }

        val hooked = state.hooked[type]

        val prevent = if (hooked != null) {
            execute(hooked).prevent
        } else {
            next(this.root.element!!)
        }

        if (reflow) reflow()
        return prevent
    }
}

data class HookKey(
    val type: String, val id: Any
)

enum class EventType {
    Click, MouseMove, MouseScroll, MouseDrag, MouseRelease, KeyPressed, KeyReleased, Type
}

class UIState {
    var focus: UIElement<*>? = null
    val hooked = hashMapOf<EventType, UIElement<*>>()
}