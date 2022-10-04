package example.examplemod.mineui.core

import example.examplemod.mineui.element.UIElement

class GuiEventContext(val element: UIElement<*>) {
    /**
     * If the event is prevented, listener of children components will be skipped
     */
    var prevent: Boolean = false
        private set
    var reflow: Boolean = false
        private set

    fun prevent() {
        prevent = true
    }

    fun requireReflow() {
        reflow = true
    }
}