package example.examplemod.mineui.element

import example.examplemod.mineui.PosXY
import example.examplemod.mineui.core.RenderNode
import example.examplemod.mineui.utils.*
import net.minecraft.client.gui.components.events.GuiEventListener

open class StyleContext {
    var size: SizeInput = FitContent

    fun size(size: Env.() -> Size) {
        this.size = DynamicSize { size() }
    }
}

abstract class UIElement<S: StyleContext>(val createStyle: () -> S): RenderNode() {
    var style: S = createStyle()
    var listener: GuiEventListener? = null

    override fun reflow(pos: PosXY, size: Size) = Unit

    fun update(style: S.() -> Unit) {
        this.style = createStyle().apply(style)
    }

    open fun getSize(): Size {
        val min = getMinimumSize()

        return when (style.size) {
            is DynamicSize -> with (style.size as DynamicSize) {
                Env(min).get()
            }
            is Size -> {
                style.size as Size
            }
        }
    }
    /**
     * Minimum Size to fit content
     */
    abstract fun getMinimumSize(): Size

    /**
     * Called after component re-rendering
     */
    open fun invalidate() {}
}