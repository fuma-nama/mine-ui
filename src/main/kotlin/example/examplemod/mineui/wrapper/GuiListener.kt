package example.examplemod.mineui.wrapper

import example.examplemod.mineui.core.GuiEventContext

open class GuiListenerBuilder {
    var onClick: (GuiEventContext.(x: Double, y: Double, type: Int) -> Unit)? = null
    var onDrag: (GuiEventContext.(
        mouseX: Double,
        mouseY: Double,
        mouseButton: Int,
        dragX: Double,
        dragY: Double,
    ) -> Unit)? = null
    var onType: (GuiEventContext.(char: Char, key: Int) -> Unit)? = null
    var onMouseRelease: (GuiEventContext.(x: Double, y: Double, mouseButton: Int) -> Unit)? = null
    var onMouseMove: (GuiEventContext.(x: Double, y: Double) -> Unit)? = null
    var onScroll: (GuiEventContext.(x: Double, y: Double, scroll: Double) -> Unit)? = null

    fun click(handler: GuiEventContext.(x: Double, y: Double, type: Int) -> Unit) {
        onClick = handler
    }

    fun scroll(handler: GuiEventContext.(x: Double, y: Double, scroll: Double) -> Unit) {
        onScroll = handler
    }

    fun mouseRelease(handler: GuiEventContext.(x: Double, y: Double, mouseButton: Int) -> Unit) {
        onMouseRelease = handler
    }

    fun mouseMove(handler: GuiEventContext.(x: Double, y: Double) -> Unit) {
        onMouseMove = handler
    }

    fun drag(
        handler: GuiEventContext.(
            mouseX: Double,
            mouseY: Double,
            mouseButton: Int,
            dragX: Double,
            dragY: Double,
        ) -> Unit,
    ) {
        this.onDrag = handler
    }

    fun type(handler: GuiEventContext.(char: Char, key: Int) -> Unit) {
        this.onType = handler
    }

    fun buildListener(): GUIListener {
        return object : GUIListener {
            override fun onClick(x: Double, y: Double, mouseButton: Int, context: GuiEventContext) {
                onClick?.invoke(context, x, y, mouseButton)
            }

            override fun onMouseMoved(x: Double, y: Double, context: GuiEventContext) {
                onMouseMove?.invoke(context, x, y)
            }

            override fun onMouseReleased(x: Double, y: Double, mouseButton: Int, context: GuiEventContext) {
                onMouseRelease?.invoke(context, x, y, mouseButton)
            }

            override fun onScroll(x: Double, y: Double, scroll: Double, context: GuiEventContext) {
                onScroll?.invoke(context, x, y, scroll)
            }

            override fun onDrag(
                mouseX: Double,
                mouseY: Double,
                mouseButton: Int,
                dragX: Double,
                dragY: Double,
                context: GuiEventContext,
            ) {
                onDrag?.invoke(context, mouseX, mouseY, mouseButton, dragX, dragY)
            }

            override fun onType(char: Char, key: Int, context: GuiEventContext) {
                onType?.invoke(context, char, key)
            }
        }
    }
}

interface GUIListener {
    fun onClick(x: Double, y: Double, mouseButton: Int, context: GuiEventContext) = Unit
    fun onDrag(
        mouseX: Double,
        mouseY: Double,
        mouseButton: Int,
        dragX: Double,
        dragY: Double,
        context: GuiEventContext,
    ) = Unit
    fun onType(char: Char, key: Int, context: GuiEventContext) = Unit

    fun onMouseMoved(x: Double, y: Double, context: GuiEventContext) = Unit

    fun onMouseReleased(x: Double, y: Double, mouseButton: Int, context: GuiEventContext) = Unit

    fun onScroll(x: Double, y: Double, scroll: Double, context: GuiEventContext) = Unit

    fun onKeyPressed(p_94745_: Int, p_94746_: Int, p_94747_: Int, context: GuiEventContext) = Unit

    fun onKeyReleased(p_94750_: Int, p_94751_: Int, p_94752_: Int, context: GuiEventContext) = Unit
}