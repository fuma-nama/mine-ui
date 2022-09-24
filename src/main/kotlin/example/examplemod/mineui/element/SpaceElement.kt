package example.examplemod.mineui.element

import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStack

class SpaceStyle : StyleContext()

class SpaceElement : UIElement<SpaceStyle>(::SpaceStyle) {
    override fun draw(stack: DrawStack, size: Size) = Unit

    override fun getMinimumSize() = Size.Empty
}