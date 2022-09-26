package example.examplemod.mineui.element

import example.examplemod.mineui.style.ImageFit
import example.examplemod.mineui.utils.*
import example.examplemod.mineui.wrapper.DrawStack
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import org.lwjgl.opengl.GL11

class ImageStyle: StyleContext() {
    var width: Int? = null
    var height: Int? = null

    override var size: SizeInput = DynamicSize {
        val src = src?: return@DynamicSize Size.Empty

        calcSize(width, height, src)
    }

    var objectFit: ImageFit = ImageFit.Stretch
    var src: ResourceLocation? = null
}

class ImageElement : UIElement<ImageStyle>(::ImageStyle) {
    override fun draw(stack: DrawStack, size: Size) {
        val src = style.src

        if (src != null) {
            val img = style.objectFit.calcDrawSize(style.width, style.height, src)

            stack.drawImage(0, 0, 0, 0, img.width, img.height, size.width, size.height, src)
        }
    }

    override fun getMinimumSize(): Size {
        val src = style.src?: return Size.Empty
        Minecraft.getInstance().textureManager.bindForSetup(src)

        val originalW = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH)
        val originalH = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT)
        return Size(originalW, originalH)
    }
}