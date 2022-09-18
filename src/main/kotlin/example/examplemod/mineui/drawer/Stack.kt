package example.examplemod.mineui.drawer

import example.examplemod.mineui.context.DrawContext
import example.examplemod.mineui.context.MouseContext
import example.examplemod.mineui.props.ContainerStyle

class Stack(style: ContainerStyle) : Container(style) {
    override fun DrawContext.drawChild() {
        for (child in rendered.children.values) {
            child.draw()
            val childInfo = child.drawer.getInfo(this)
            val add = childInfo.width + style.gap

            stack.translate(add.toDouble(), 0.0, 0.0)
        }
    }

    override fun getContentInfo(context: MouseContext): Info {
        var w = 0
        var maxH = 0

        for ((i, child) in rendered.children.values.withIndex()) {
            if (i != 0) {
                w += style.gap
            }

            val info = child.drawer.getInfo(context)
            w += info.width
            maxH = maxH.coerceAtLeast(info.height)
        }

        return Info(width = w, height = maxH)
    }
}