package example.examplemod.mineui.utils

import example.examplemod.mineui.style.ImageFit
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import org.lwjgl.opengl.GL11

data class Size(val width: Int, val height: Int): SizeInput {
    operator fun plus(other: Size): Size {
        return Size(width + other.width, height + other.height)
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

fun ImageFit.calcDrawSize(width: Int?, height: Int?, src: ResourceLocation): Size {
    Minecraft.getInstance().textureManager.bindForSetup(src)

    val originalW = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH)
    val originalH = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT)

    if (width == null && height == null) {
        return Size(originalW, originalH)
    }

    val scaleX = width?.let { it.toDouble() / originalW }
    val scaleY = height?.let { it.toDouble() / originalH }

    return when (this) {
        ImageFit.Stretch -> {
            val scaledW = originalW * (scaleX?: scaleY!!)
            val scaledH = originalH * (scaleY?: scaleX!!)

            Size(scaledW.toInt(), scaledH.toInt())
        }
        ImageFit.Contain -> {
            val minScale = min(scaleX, scaleY)!!

            Size((originalW * minScale).toInt(), (originalH * minScale).toInt())
        }
        ImageFit.Cover -> {
            val maxScale = max(scaleX, scaleY)!!

            Size((originalW * maxScale).toInt(), (originalH * maxScale).toInt())
        }
    }
}