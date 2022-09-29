package example.examplemod.mineui.core

import example.examplemod.mineui.element.UIElement

class GuiEventContext(val ui: UI, val element: UIElement<*>) {
    /**
     * If the event is prevented, listener of children components will be skipped
     */
    var prevent: Boolean = false
    var reflow: Boolean = false
}