package example.examplemod.mineui

import example.examplemod.mineui.core.Component
import example.examplemod.mineui.element.*
import example.examplemod.mineui.element.layout.*

fun Component.label(style: LabelStyle.() -> Unit = {}, text: () -> String) = label(text::class, style, text)

fun Component.label(key: Any, style: LabelStyle.() -> Unit = {}, text: () -> String) = child(key) {
    element(::TextElement, style).text = text()
}

fun Component.button(style: ButtonStyle.() -> Unit = {}, content: () -> String) = button(content::class, style, content())

fun Component.button(key: Any, style: ButtonStyle.() -> Unit = {}, content: String) = child(key) {
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


fun Component.simpleGrid(style: SimpleGridLayoutStyle.() -> Unit, children: Component.() -> Unit) =
    simpleGrid(children::class, style, children)

fun Component.simpleGrid(key: Any, style: SimpleGridLayoutStyle.() -> Unit, children: Component.() -> Unit) = child(key) {
    element(::SimpleGridLayout, style)

    children(this)
}

fun Component.box(style: ContainerStyle.() -> Unit, children: Component.() -> Unit) =
    box(children::class, style, children)

fun Component.box(key: Any, style: ContainerStyle.() -> Unit, children: Component.() -> Unit) = child(key) {
    element(::BoxElementImpl, style)

    children(this)
}