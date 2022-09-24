package example.examplemod.mineui.core

import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStack

abstract class RenderNode {
    open val children: List<RenderNode> = emptyList()
    private lateinit var position: PosXY
    private lateinit var size: Size

    open val absolutePosition by ::position
    open val absoluteSize by ::size

    fun drawNode(stack: DrawStack) {
        stack.translated = absolutePosition
        draw(stack, size)

        for (element in children) {
            element.drawNode(stack)
        }
    }

    fun reflowNode(pos: PosXY, size: Size) {
        this.position = pos
        this.size = size

        reflow(pos, size)
    }

    abstract fun draw(stack: DrawStack, size: Size)

    abstract fun reflow(pos: PosXY, size: Size)
}