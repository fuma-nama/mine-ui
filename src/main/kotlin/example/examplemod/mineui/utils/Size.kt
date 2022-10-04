package example.examplemod.mineui.utils

import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import org.lwjgl.opengl.GL11

data class Size(val width: Int, val height: Int): SizeInput {
    operator fun plus(other: Size): Size {
        return Size(width + other.width, height + other.height)
    }
    operator fun minus(other: Size): Size {
        return Size(width - other.width, height - other.height)
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