package example.examplemod.mineui.element

open class ContainerStyle : BoxStyle() {
    var gap: Int = 0
}

abstract class Container<S: ContainerStyle>(create: () -> S) : BoxElement<S>(create) {
    override val children: ArrayList<UIElement<*>> = arrayListOf()

    override fun invalidate() {
        children.clear()
    }
}