package example.examplemod.mineui.core

import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStack
import example.examplemod.mineui.wrapper.lockState

abstract class RenderNode {
    open val children: List<RenderNode> = emptyList()
    lateinit var absolutePosition: PosXY
        private set
    lateinit var absoluteSize: Size
        private set

    open fun bindPosition(original: PosXY): PosXY = original

    open fun drawNode(stack: DrawStack) {
        stack.translated = absolutePosition

        stack.lockState {
            draw(stack, absoluteSize)
            drawChildren(stack)
        }

        drawFilter(stack, absoluteSize)
    }

    open fun drawChildren(stack: DrawStack) {
        for (element in children) {
            element.drawNode(stack)
        }
    }

    /**
     * Things Drawn after its children
     */
    open fun drawFilter(stack: DrawStack, size: Size) = Unit

    fun reflowNode(pos: PosXY, size: Size) {
        this.absolutePosition = bindPosition(pos)
        this.absoluteSize = size

        reflow(pos, size)
    }

    abstract fun draw(stack: DrawStack, size: Size)

    abstract fun reflow(pos: PosXY, size: Size)
}

fun RenderNode.isIn(x: Double, y: Double): Boolean {
    return x >= absolutePosition.x && x <= absolutePosition.x + absoluteSize.width
            &&
            y >= absolutePosition.y && y <= absolutePosition.y + absoluteSize.height
}