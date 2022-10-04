package example.examplemod.mineui.element

import example.examplemod.mineui.core.Component
import example.examplemod.mineui.core.GuiEventContext
import example.examplemod.mineui.core.RenderNode
import example.examplemod.mineui.core.UI
import example.examplemod.mineui.style.DynamicPosition
import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.style.PositionInput
import example.examplemod.mineui.style.Relative
import example.examplemod.mineui.utils.*
import example.examplemod.mineui.wrapper.GUIListener
import example.examplemod.mineui.wrapper.GuiListenerBuilder

open class StyleContext: GuiListenerBuilder() {
    var position: PositionInput = Relative
    var requireFocus: Boolean = false

    var hover: (() -> Unit)? = null
    var focus: (() -> Unit)? = null

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
fun<T: StyleContext> T.focus(on: T.() -> Unit) {
    focus = {
        on(this)
    }
}
fun<T: StyleContext> T.hover(on: T.() -> Unit) {
    hover = {
        on(this)
    }
}

abstract class UIElement<S: StyleContext>(val createStyle: () -> S): RenderNode(), GUIListener {
    var hovered: Boolean = false
    var focused: Boolean = false
    var styleSheet: S.() -> Unit = {}

    lateinit var ui: UI
    var style: S = createStyle()
    var listener: GUIListener? = null

    open fun init(ui: UI, component: Component) {
        this.ui = ui
    }

    fun updateUiState(hovered: Boolean, focused: Boolean): Boolean {
        val reflow = this.hovered != hovered || this.focused != focused

        if (reflow) {
            this.hovered = hovered
            this.focused = focused

            updateStyle()
        }
        return reflow
    }

    override fun onClick(x: Double, y: Double, mouseButton: Int, context: GuiEventContext) {
        if (style.requireFocus) {
            ui.focus(this)
            context.prevent()
        } else {
            ui.focus(null)
        }
    }

    override fun reflow(pos: PosXY, size: Size) = Unit

    override fun bindPosition(original: PosXY): PosXY {
        return when (style.position) {
            is DynamicPosition -> (style.position as DynamicPosition).receive(
                original
            )
            is PosXY -> style.position as PosXY
        }
    }

    open fun updateStyle(style: S.() -> Unit = styleSheet) {
        this.styleSheet = style
        this.style = createStyle().apply(style).apply {
            if (hovered) hover?.invoke()
            if (focused) focus?.invoke()
        }
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