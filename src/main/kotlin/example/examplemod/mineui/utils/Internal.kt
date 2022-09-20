package example.examplemod.mineui.utils

import example.examplemod.mineui.core.Component
import example.examplemod.mineui.element.UIElement

fun extractChildren(comp: Component): List<UIElement<*>> {
    val children = arrayListOf<UIElement<*>>()
    val element = comp.element

    if (element != null) {
        children += element
    } else {
        for (child in comp.children.values) {
            children += extractChildren(comp)
        }
    }

    return children
}