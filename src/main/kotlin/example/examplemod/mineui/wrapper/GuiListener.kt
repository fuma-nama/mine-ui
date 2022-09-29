package example.examplemod.mineui.wrapper

import example.examplemod.mineui.core.GuiEventContext

open class GuiListenerBuilder {
    var onClick: (GuiEventContext.(x: Double, y: Double, type: Int) -> Unit)? = null
    var onDrag: (GuiEventContext.(
        mouseX: Double,
        mouseY: Double,
        mouseButton: Int,
        dragX: Double,
        dragY: Double
    ) -> Unit)? = null

    fun click(handler: GuiEventContext.(x: Double, y: Double, type: Int) -> Unit) {
        onClick = handler
    }

    fun buildListener(): GUIListener {
        return object : GUIListener {
            override fun onClick(x: Double, y: Double, mouseButton: Int, context: GuiEventContext) {
                onClick?.invoke(context, x, y, mouseButton)
            }

            override fun onDrag(
                mouseX: Double,
                mouseY: Double,
                mouseButton: Int,
                dragX: Double,
                dragY: Double,
                context: GuiEventContext
            ) {
                onDrag?.invoke(context, mouseX, mouseY, mouseButton, dragX, dragY)
            }
        }
    }
}

open class NestGUIListener(val base: GUIListener?): GUIListener {
    override fun onClick(x: Double, y: Double, mouseButton: Int, context: GuiEventContext) {
        base?.onClick(x, y, mouseButton, context)
    }

    override fun onDrag(mouseX: Double, mouseY: Double, mouseButton: Int, dragX: Double, dragY: Double, context: GuiEventContext) {
        base?.onDrag(mouseX, mouseY, mouseButton, dragX, dragY, context)
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
        context: GuiEventContext
    ) = Unit
}