package example.examplemod.mineui.drawer

import example.examplemod.mineui.DrawContext

fun interface Drawer {
    fun DrawContext.draw()
}

class DefaultDrawer: Drawer {
    override fun DrawContext.draw() {
        for (child in rendered.children.values) {
            child.draw(stack)
        }
    }
}
