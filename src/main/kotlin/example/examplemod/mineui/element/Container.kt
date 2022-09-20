package example.examplemod.mineui.element

data class Point4(
    val top: Int,
    val left: Int,
    val right: Int,
    val bottom: Int
) {
    val px: Int get() = left + right
    val py: Int get() = top + bottom
}

open class ContainerStyle : BoxStyle() {
    var gap: Int = 0
}

abstract class Container<S: ContainerStyle>(create: () -> S) : BoxElement<S>(create) {
    var children: ArrayList<UIElement<*>> = arrayListOf()

    override fun prepare() {
        children.forEach { child ->
            child.prepare()
        }

        super.prepare()
    }

    override fun invalidate() {
        children.clear()
    }
}