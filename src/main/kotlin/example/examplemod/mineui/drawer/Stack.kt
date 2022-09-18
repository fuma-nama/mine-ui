package example.examplemod.mineui.drawer

import example.examplemod.mineui.DrawContext

class Stack : Drawer {
    override fun DrawContext.draw() {
        var left = 0.0

        for (child in rendered.children.values) {
            stack.translate(left, 0.0, 0.0)
            child.draw(stack)
            left += 100
        }
    }
}