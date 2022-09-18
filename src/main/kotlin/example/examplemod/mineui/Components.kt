package example.examplemod.mineui

import example.examplemod.mineui.drawer.Label
import example.examplemod.mineui.drawer.Stack
import example.examplemod.mineui.props.ContainerStyle
import java.awt.Color

fun RenderContext.label(text: () -> String) = label(text::class, text)

fun RenderContext.label(key: Any, text: () -> String) = child(key) {
    drawer = Label(text())
}

fun RenderContext.stack(style: ContainerStyle.() -> Unit = {}, children: RenderContext.() -> Unit) =
    stack(children::class, style, children)

fun RenderContext.stack(
    key: Any,
    style: ContainerStyle.() -> Unit = {},
    children: RenderContext.() -> Unit) = child(key) {
    drawer = Stack(ContainerStyle().apply(style))

    children(this) //render children
}

open class StyleProps {
    var color: Color = Color.WHITE
}