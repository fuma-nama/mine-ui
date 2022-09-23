package example.examplemod.mineui.element

open class ContainerStyle : BoxStyle() {
}

abstract class Container<S: ContainerStyle>(create: () -> S) : BoxElement<S>(create) {
    override val children: ArrayList<UIElement<*>> = arrayListOf()

    override fun invalidate() {
        children.clear()
    }
}