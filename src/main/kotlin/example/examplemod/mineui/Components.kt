package example.examplemod.mineui

import example.examplemod.mineui.core.Component
import example.examplemod.mineui.element.*
import example.examplemod.mineui.element.input.*
import example.examplemod.mineui.element.layout.*
import example.examplemod.mineui.utils.Size

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

fun Component.simpleGrid(style: SimpleGridLayoutStyle.() -> Unit = {}, children: Component.() -> Unit) =
    simpleGrid(children::class, style, children)

fun Component.simpleGrid(key: Any, style: SimpleGridLayoutStyle.() -> Unit = {}, children: Component.() -> Unit) = child(key) {
    element(::SimpleGridLayout, style)

    children(this)
}

fun Component.box(style: ContainerStyle.() -> Unit = {}, children: Component.() -> Unit) =
    box(children::class, style, children)

fun Component.box(key: Any, style: ContainerStyle.() -> Unit = {}, children: Component.() -> Unit) = child(key) {
    element(::BoxLayout, style)

    children(this)
}

fun Component.column(style: ColumnStyle.() -> Unit = {}, children: Component.() -> Unit) =
    column(children::class, style, children)

fun Component.column(key: Any, style: ColumnStyle.() -> Unit = {}, children: Component.() -> Unit) = child(key) {
    element(::ColumnLayout, style)

    children(this)
}

fun Component.row(style: RowStyle.() -> Unit = {}, children: Component.() -> Unit) =
    row(children::class, style, children)

fun Component.row(key: Any, style: RowStyle.() -> Unit = {}, children: Component.() -> Unit) = child(key) {
    element(::RowLayout, style)

    children(this)
}

fun Component.mask(style: MaskLayoutStyle.() -> Unit = {}, children: Component.() -> Unit) =
    mask(children::class, style, children)

fun Component.mask(key: Any, style: MaskLayoutStyle.() -> Unit = {}, children: Component.() -> Unit) = child(key) {
    element(::MaskLayout, style)

    children(this)
}

fun Component.space(size: () -> Size) = space(size::class, size())

fun Component.space(key: Any, size: Size) = child(key) {
    element(::SpaceElement) {
        this.size = size
    }
}

fun Component.absolute(style: AbsoluteStyle.() -> Unit = {}, children: Component.() -> Unit) =
    absolute(children::class, style, children)

fun Component.absolute(key: Any, style: AbsoluteStyle.() -> Unit = {}, children: Component.() -> Unit) = child(key) {
    element(::AbsoluteLayout, style)

    children(this)
}

fun Component.image(style: ImageStyle.() -> Unit) = image(style::class, style)

fun Component.image(key: Any, style: ImageStyle.() -> Unit) = child(key) {
    element(::ImageElement, style)
}
fun Component.textField(
    style: TextFieldStyle.() -> Unit,
) = textField(style::class, style)

fun Component.textField(
    key: Any,
    style: TextFieldStyle.() -> Unit,
) = child(key) {
    element(::TextFieldElement, style)
}

fun Component.textArea(
    style: TextAreaStyle.() -> Unit
) = child(style::class) {
    element(::TextAreaElement, style)
}

fun Component.textArea(
    key: Any,
    style: TextAreaStyle.() -> Unit = {}
) = child(key) {
    element(::TextAreaElement, style)
}

fun Component.checkbox(style: CheckboxStyle.() -> Unit) = checkbox(style::class, style)
fun Component.checkbox(key: Any, style: CheckboxStyle.() -> Unit = {}) = child(key) {
    element(::CheckboxElement, style)
}