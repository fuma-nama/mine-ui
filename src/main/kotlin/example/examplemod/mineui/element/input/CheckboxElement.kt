package example.examplemod.mineui.element.input

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import example.examplemod.mineui.core.GuiEventContext
import example.examplemod.mineui.element.StyleContext
import example.examplemod.mineui.element.UIElement
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStack
import net.minecraft.resources.ResourceLocation
import java.awt.Color

sealed interface CheckIcon
data class ImageIcon(val checked: ResourceLocation? = null, val unchecked: ResourceLocation? = null): CheckIcon

data class DefaultIcon(
    val color: Color = Color.WHITE,
    val background: Color = Color.BLACK,
): CheckIcon {
    companion object {
        val Texture = ResourceLocation("textures/gui/checkbox.png")
    }
}

class CheckboxStyle : StyleContext() {
    var value: Boolean = false
    var onChange: (Boolean) -> Unit = {}
    var icon: CheckIcon = DefaultIcon()

    fun icon(color: Color = Color.WHITE, background: Color = Color.BLACK) {
        this.icon = DefaultIcon(color, background)
    }

    fun icon(checked: ResourceLocation, unchecked: ResourceLocation) {
        this.icon = ImageIcon(checked, unchecked)
    }

    init {
        size(15, 15)
    }
}

class CheckboxElement : UIElement<CheckboxStyle>(::CheckboxStyle) {
    override fun draw(stack: DrawStack, size: Size) {
        stack.drawCheckedIcon(style.value, size, style.icon)
    }

    override fun onClick(x: Double, y: Double, mouseButton: Int, context: GuiEventContext) {
        context.reflow = true
        style.onChange(!style.value)
        super.onClick(x, y, mouseButton, context)
    }

    override fun getMinimumSize() = Size.Empty
}

fun DrawStack.drawCheckedIcon(checked: Boolean, size: Size, icon: CheckIcon) {
    when (icon) {
        is ImageIcon -> {
            val image = if (checked) icon.checked else icon.unchecked

            if (image != null) {
                drawImage(0, 0, size.width, size.height, image)
            }
        }
        is DefaultIcon -> {
            fillRect(0, 0, size.width, size.height, icon.background)

            if (checked) {
                RenderSystem.enableBlend()
                RenderSystem.defaultBlendFunc()
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA)
                val scaleX = size.width / 20.0
                val scaleY = size.height / 20.0

                drawImage(
                    0, 0,
                    (20 * scaleX).toInt(),
                    (20 * scaleY).toInt(),
                    size.width, size.height,
                    (64 * scaleX).toInt(), (64 * scaleY).toInt(),
                    DefaultIcon.Texture
                )
            }
        }
    }
}