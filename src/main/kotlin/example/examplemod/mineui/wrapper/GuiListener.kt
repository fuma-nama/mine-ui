package example.examplemod.mineui.wrapper

import net.minecraft.client.gui.components.events.GuiEventListener

open class GuiListenerBuilder {
    var onClick: ((x: Double, y: Double, type: Int) -> Boolean)? = null

    fun click(handler: (x: Double, y: Double, type: Int) -> Boolean) {
        onClick = handler
    }

    fun clickDefault(handler: (x: Double, y: Double, type: Int) -> Unit) {
        onClick = {x, y, type ->
            handler(x, y, type)
            true
        }
    }

    fun buildListener(): GuiEventListener {
        return object : GuiEventListener {
            override fun mouseClicked(x: Double, y: Double, type: Int): Boolean {
                return onClick?.invoke(x, y, type)?: false
            }
        }
    }
}