package example.examplemod.mineui.element

import example.examplemod.mineui.style.Border
import example.examplemod.mineui.style.ImageFit
import example.examplemod.mineui.style.Point4
import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.utils.drawImage
import example.examplemod.mineui.wrapper.DrawStack
import net.minecraft.resources.ResourceLocation
import java.awt.Color

interface BoxBuilder {
    var padding: Point4
    var border: Border?
    var borderColor: Color?
        get() = border?.color
        set(v) {
            border?.let {
                border = it.copy(color = v?: it.color)
            }
        }

    fun border(x: Int = 0, y: Int = 0, color: Color) = border(y, x, x, y, color)

    fun border(top: Int = 0, left: Int = 0, right: Int = 0, bottom: Int = 0, color: Color) {
        border = Border(
            Point4(top, left, right, bottom),
            color
        )
    }

    fun padding(top: Int, left: Int, right: Int, bottom: Int) {
        padding = Point4(top, left, right, bottom)
    }

    fun padding(px: Int, py: Int) {
        padding = Point4(py, px, px, py)
    }

    fun padding(p: Int) {
        padding = Point4(p, p, p, p)
    }
}

open class BoxStyle: StyleContext(), BoxBuilder {
    override var border: Border? = null
    override var padding = Point4.Empty
    open var background: Color? = null
    open var backgroundImage: ResourceLocation? = null

    /**
     * Size of background image
     */
    var backgroundSize: Size? = null
    var backgroundFit: ImageFit = ImageFit.Stretch
}

abstract class BoxElement<S: BoxStyle>(create: () -> S): UIElement<S>(create) {
    abstract fun getContentSize(): Size

    override fun reflow(pos: PosXY, size: Size) {
        val padding = style.padding

        val inner = Size(
            width = size.width - padding.px,
            height = size.height - padding.py
        )

        reflowContent(pos, PosXY(padding.left, padding.right), inner)
    }

    /**
     * @param size Size of content
     */
    open fun reflowContent(pos: PosXY, padding: PosXY, size: Size) = Unit

    override fun getMinimumSize(): Size {
        val content = getContentSize()
        val border = style.border?.size

        return content.plus(
            width = style.padding.px + (border?.px?: 0),
            height = style.padding.py + (border?.py?: 0)
        )
    }

    override fun draw(stack: DrawStack, size: Size) {
        var size = size
        val padding = style.padding
        val border = style.border?.size

        style.border?.let {
            stack.drawBorder(0, 0, size.width, size.height, it.color, it.size)
            stack.translate(it.size.left, it.size.top)

            size = size.minus(
                border?.px?: 0,
                border?.py?: 0
            )
        }

        drawBackground(stack, size)
        stack.translate(padding.left, padding.right)

        drawContent(stack, size
            .minus(
                style.padding.px,
                style.padding.py
            )
        )
    }

    fun drawBackground(stack: DrawStack, size: Size) {
        with(style) {
            if (background != null) {
                stack.fillRect(0, 0, size.width, size.height, background!!)
            }
            if (backgroundImage != null) {
                stack.drawImage(backgroundFit, backgroundSize ?: size, backgroundImage!!)
            }
        }
    }

    open fun drawContent(stack: DrawStack, size: Size) = Unit

    val contentOffset: PosXY get() {
        val border = style.border?.size

        return PosXY(
            x = style.padding.left + (border?.left?: 0),
            y = style.padding.right + (border?.top?: 0)
        )
    }
}