package example.examplemod.mineui

import example.examplemod.mineui.core.Component
import example.examplemod.mineui.core.UI
import example.examplemod.mineui.utils.Size

@DslMarker
annotation class DslBuilder

fun component(width: Int, height: Int, render: Component.() -> Unit): UI {
    return UI(Size(width, height), render)
}

fun component(render: Component.() -> Unit): UI {
    return UI(null, render)
}