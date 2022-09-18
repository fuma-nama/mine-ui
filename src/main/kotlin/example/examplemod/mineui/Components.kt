package example.examplemod.mineui

import example.examplemod.mineui.context.RenderContext
import example.examplemod.mineui.drawer.Label
import example.examplemod.mineui.drawer.Stack
import example.examplemod.mineui.style.ContainerStyle
import example.examplemod.mineui.style.LabelStyle

fun RenderContext.label(style: LabelStyle.() -> Unit = {}, text: () -> String) = label(text::class, style, text)

fun RenderContext.label(key: Any, style: LabelStyle.() -> Unit = {}, text: () -> String) = child(key) {
    drawer = Label(text(), LabelStyle().apply(style))
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