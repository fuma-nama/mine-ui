package example.examplemod.mineui.utils

import example.examplemod.mineui.style.ImageFit
import example.examplemod.mineui.wrapper.DrawStack
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import org.lwjgl.opengl.GL11

data class Size(val width: Int, val height: Int): SizeInput {
    operator fun plus(other: Size): Size {
        return Size(width + other.width, height + other.height)
    }

    fun plus(width: Int = 0, height: Int = 0): Size {
        return Size(this.width + width, this.height + height)
    }

    fun minus(width: Int = 0, height: Int = 0): Size {
        return Size(this.width - width, this.height - height)
    }

    /**
     * Combine a new Size object with greatest size from two sizes
     */
    fun combineMax(other: Size): Size {
        return Size(
            other.width.coerceAtLeast(width),
            other.height.coerceAtLeast(height)
        )
    }

    companion object {
        val Empty = Size(0, 0)
    }
}

object FitContent : DynamicSize {
    override fun Env.get() = content
}

fun interface DynamicSize : SizeInput {
    fun Env.get(): Size
}

data class Env(
    val content: Size,
)

sealed interface SizeInput

fun max(n1: Double?, n2: Double?): Double? {
    return when {
        n1 != null -> {
            max(n1, n2)
        }
        n2 != null -> {
            max(n2, n1)
        }
        else -> null
    }
}

fun max(n1: Double, n2: Double?): Double {
    return if (n2 != null) {
        n1.coerceAtMost(n2)
    } else {
        n1
    }
}

fun min(n1: Double?, n2: Double?): Double? {
    return when {
        n1 != null -> {
            min(n1, n2)
        }
        n2 != null -> {
            min(n2, n1)
        }
        else -> null
    }
}

fun min(n1: Double, n2: Double?): Double {
    return if (n2 != null) {
        n1.coerceAtLeast(n2)
    } else {
        n1
    }
}

fun calcSize(width: Int?, height: Int?, src: ResourceLocation): Size {
    if (width != null && height != null) {
        return Size(width, height)
    }

    Minecraft.getInstance().textureManager.bindForSetup(src)

    val originalW = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH)
    val originalH = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT)
    val scale = when {
        height != null -> height.toDouble() / originalH
        width != null -> width.toDouble() / originalW
        else -> 1.0
    }

    return Size((originalW * scale).toInt(), (originalH * scale).toInt())
}

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

            drawImage(0, 0, 0, 0, size.width, size.height, scaledW.toInt(), scaledH.toInt(), src)
        }
    }
}