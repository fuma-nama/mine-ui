package example.examplemod.mineui.utils

import example.examplemod.mineui.core.Component
import example.examplemod.mineui.element.UIElement
import java.awt.Color

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

fun Color.edit(
    red: Float = this.red / 255f,
    green: Float = this.green / 255f,
    blue: Float = this.blue / 255f,
    alpha: Float = this.alpha / 255f
): Color {
    return Color(red, green, blue, alpha)
}

fun Color.edit(
    red: Int = this.red,
    green: Int = this.green,
    blue: Int = this.blue,
    alpha: Int = this.alpha): Color {
    return Color(red, green, blue, alpha)
}