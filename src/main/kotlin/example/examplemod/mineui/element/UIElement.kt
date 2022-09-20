package example.examplemod.mineui.element

import example.examplemod.mineui.PosXY
import example.examplemod.mineui.utils.*
import example.examplemod.mineui.wrapper.DrawStack

open class StyleContext {
    var size: SizeInput = FitContent

    fun size(size: Env.() -> Size) {
        this.size = DynamicSize { size() }
    }
}

abstract class UIElement<S: StyleContext>(val createStyle: () -> S) {
    var style: S = createStyle()

    //updated per draw
    lateinit var actualSize: Size
    lateinit var minSize: Size
    /**
     * Size that defined by the style or equal to minSize
     */
    lateinit var size: Size

    fun update(style: S.() -> Unit) {
        this.style = createStyle().apply(style)
    }

    /**
     * Minimum Size to fit content
     */
    abstract fun getMinimumSize(): Size

    /**
     * Final size with padding, margin and others
     */
    open fun getMaximumSize() = size

    /**
     * Prepare for draw
     */
    open fun prepare() {
        minSize = getMinimumSize()

        when (style.size) {
            is DynamicSize -> with (style.size as DynamicSize) {
                size = Env(minSize).get()
                actualSize = getMaximumSize()
            }
            is Size -> {
                size = minSize
                actualSize = style.size as Size
            }
        }
    }

    /**
     * Called after component re-rendering
     */
    open fun invalidate() {}

    abstract fun draw(stack: DrawStack, mouse: PosXY, size: Size = actualSize)
}