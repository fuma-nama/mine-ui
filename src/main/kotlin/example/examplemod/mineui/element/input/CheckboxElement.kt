package example.examplemod.mineui.element.input

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import example.examplemod.mineui.core.GuiEventContext
import example.examplemod.mineui.element.StyleContext
import example.examplemod.mineui.element.UIElement
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStack
import net.minecraft.resources.ResourceLocation
import kotlin.math.roundToInt

fun interface CheckIcon {
    fun draw(stack: DrawStack, checked: Boolean, size: Size)
}

data class ImageIcon(val checked: ResourceLocation? = null, val unchecked: ResourceLocation? = null): CheckIcon {
    override fun draw(stack: DrawStack, checked: Boolean, size: Size) {
        val image = if (checked) this.checked else this.unchecked

        if (image != null) {
            stack.drawImage(0, 0, size.width, size.height, image)
        }
    }
}

object DefaultIcon: CheckIcon {
    val Texture = ResourceLocation("textures/gui/checkbox.png")

    override fun draw(stack: DrawStack, checked: Boolean, size: Size) {
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA)
        val scaleX = size.width / 20.0F
        val scaleY = size.height / 20.0F

        stack.drawImage(
            0, 0,
            20F * scaleX,
            if (checked) 20F * scaleY else 0F,
            size.width, size.height,
            (64 * scaleX).roundToInt(), (64 * scaleY).roundToInt(),
            Texture
        )
    }
}

class CheckboxStyle : StyleContext() {
    var value: Boolean = false
    var onChange: (Boolean) -> Unit = {}
    var icon: CheckIcon = DefaultIcon

    fun defaultIcon() {
        this.icon = DefaultIcon
    }

    fun icon(icon: (stack: DrawStack, checked: Boolean, size: Size) -> Unit) {
        this.icon = CheckIcon { stack, checked, size ->
            icon(stack, checked, size)
        }
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
        style.icon.draw(stack, style.value, size)
    }

    override fun onClick(x: Double, y: Double, mouseButton: Int, context: GuiEventContext) {
        context.reflow = true
        style.onChange(!style.value)
        super.onClick(x, y, mouseButton, context)
    }

    override fun getMinimumSize() = Size.Empty
}