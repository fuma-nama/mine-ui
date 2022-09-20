package example.examplemod.mineui

import example.examplemod.mineui.core.Component
import example.examplemod.mineui.element.*

fun Component.label(style: LabelStyle.() -> Unit = {}, text: () -> String) = label(style::class, style, text)

fun Component.label(key: Any, style: LabelStyle.() -> Unit = {}, text: () -> String) = child(key) {
    element(::TextElement, style).text = text()
}

fun Component.button(style: ButtonStyle.() -> Unit = {}, content: () -> String) = button(content::class, content(), style)

fun Component.button(key: Any, content: String, style: ButtonStyle.() -> Unit) = child(key) {
    element(::ButtonElement, style).setText(content)
}

fun Component.stack(style: StackStyle.() -> Unit = {}, children: Component.() -> Unit) =
    stack(children::class, style, children)

fun Component.stack(
    key: Any,
    style: StackStyle.() -> Unit = {},
    children: Component.() -> Unit) = child(key) {
    element(::StackLayout, style)

    children(this) //render children
}