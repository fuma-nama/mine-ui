package example.examplemod.mineui.utils

import example.examplemod.mineui.style.ImageFit
import example.examplemod.mineui.wrapper.DrawStack
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import org.lwjgl.opengl.GL11

fun DrawStack.drawImage(fit: ImageFit, size: Size, src: ResourceLocation) {
    Minecraft.getInstance().textureManager.bindForSetup(src)

    val originalW = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH)
    val originalH = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT)

    val scaleX = size.width.toDouble() / originalW
    val scaleY = size.height.toDouble() / originalH

    when (fit) {
        ImageFit.Stretch -> {
            val scaledW = originalW * scaleX
            val scaledH = originalH * scaleY

            drawImage(0, 0, scaledW.toInt(), scaledH.toInt(), src)
        }
        ImageFit.Contain -> {
            val maxScale = max(scaleX, scaleY)
            val scaledW = originalW * maxScale
            val scaledH = originalH * maxScale

            drawImage(0, 0, scaledW.toInt(), scaledH.toInt(), src)
        }
        ImageFit.Cover -> {
            val minScale = min(scaleX, scaleY)
            val scaledW = originalW * minScale
            val scaledH = originalH * minScale

            drawImage(0, 0, 0f, 0f, size.width, size.height, scaledW.toInt(), scaledH.toInt(), src)
        }
    }
}