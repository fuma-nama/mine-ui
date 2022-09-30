package example.examplemod.mineui.element

import example.examplemod.mineui.element.internal.ScrollView
import example.examplemod.mineui.element.internal.ScrollViewStyle

open class ContainerStyle : ScrollViewStyle() {
}

abstract class Container<S: ContainerStyle>(create: () -> S) : ScrollView<S>(create) {
    override val children: ArrayList<UIElement<*>> = arrayListOf()

    override fun invalidate() {
        children.clear()
    }
}