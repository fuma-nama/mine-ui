package example.examplemod.mineui.utils

data class Size(val width: Int, val height: Int): SizeInput

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