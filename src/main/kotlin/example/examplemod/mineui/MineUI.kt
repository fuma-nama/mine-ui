package example.examplemod.mineui

import example.examplemod.mineui.context.RenderContext

@DslMarker
annotation class DslBuilder

fun component(render: RenderContext.() -> Unit): UI {
    return UI(render)
}