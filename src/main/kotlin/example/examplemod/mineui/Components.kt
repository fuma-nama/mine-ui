package example.examplemod.mineui

import example.examplemod.mineui.drawer.Drawer
import example.examplemod.mineui.drawer.Stack
import java.awt.Color

fun RenderContext.label(text: () -> String) = label(text::class, text)

fun RenderContext.label(key: Any, text: () -> String) = child(key) {
    drawer = Drawer {
        font.draw(stack, text(), 0F, 0F, Color.WHITE.rgb)
    }
}

fun RenderContext.stack(children: RenderContext.() -> Unit) = stack(children::class, children)

fun RenderContext.stack(key: Any, children: RenderContext.() -> Unit) = child(key) {
    drawer = Stack()

    children(this) //render children
}

open class StyleProps {
    var color: Color = Color.WHITE
}