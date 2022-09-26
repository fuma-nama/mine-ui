package example.examplemod.mineui.element

import example.examplemod.mineui.core.RenderNode
import example.examplemod.mineui.style.DynamicPosition
import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.style.PositionInput
import example.examplemod.mineui.style.Relative
import example.examplemod.mineui.utils.*
import example.examplemod.mineui.wrapper.GuiListenerBuilder
import net.minecraft.client.gui.components.events.GuiEventListener

open class StyleContext: GuiListenerBuilder() {
    var position: PositionInput = Relative

    open var size: SizeInput = FitContent

    fun size(width: Int, height: Int) {
        size = Size(width, height)
    }

    fun size(size: Env.() -> Size) {
        this.size = DynamicSize { size() }
    }

    fun position(x: Int, y: Int) {
        position = PosXY(x, y)
    }

    fun position(dynamic: (PosXY) -> PosXY) {
        position = DynamicPosition { dynamic(it) }
    }
}

abstract class UIElement<S: StyleContext>(val createStyle: () -> S): RenderNode() {
    var style: S = createStyle()
    var listener: GuiEventListener? = null

    override fun reflow(pos: PosXY, size: Size) = Unit

    override val absolutePosition: PosXY
        get() = when (style.position) {
            is DynamicPosition -> (style.position as DynamicPosition).receive(
                super.absolutePosition
            )
            is PosXY -> style.position as PosXY
        }

    fun update(style: S.() -> Unit) {
        this.style = createStyle().apply(style)
        this.listener = this.style.buildListener()
    }

    open fun getSize(): Size {

        return when (style.size) {
            is DynamicSize -> with (style.size as DynamicSize) {
                val min = getMinimumSize()

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