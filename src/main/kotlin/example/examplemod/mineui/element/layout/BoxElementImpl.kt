package example.examplemod.mineui.element.layout

import example.examplemod.mineui.element.Container
import example.examplemod.mineui.element.ContainerStyle
import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.utils.Size

class BoxElementImpl : Container<ContainerStyle>(::ContainerStyle) {

    override fun getContentSize(): Size {
        val child = children.firstOrNull()
        return child?.getSize()?: Size(0, 0)
    }

    override fun reflowContent(pos: PosXY, padding: PosXY, size: Size) {
        val offset = pos + padding

        for (child in children) {
            child.reflowNode(offset, size)
        }
    }
}