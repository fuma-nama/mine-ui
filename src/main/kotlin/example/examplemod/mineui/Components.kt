package example.examplemod.mineui

import java.awt.Color

fun RenderContext.label(text: () -> String) = child(text::class) {
    drawer = Drawer {
        font.draw(stack, text(), 0F, 0F, Color.WHITE.rgb)
    }
}

fun RenderContext.stack(key: Any, children: RenderContext.() -> Unit) = child(key) {
    drawer = Drawer {
        for (child in rendered.children.values) {
            child.draw(stack)
        }
    }

    children(this) //render children
}

open class StyleProps {
    var color: Color = Color.WHITE
}