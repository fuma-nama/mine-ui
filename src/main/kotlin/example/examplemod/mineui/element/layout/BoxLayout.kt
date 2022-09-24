package example.examplemod.mineui.element.layout

import example.examplemod.mineui.element.Container
import example.examplemod.mineui.element.ContainerStyle
import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.utils.Size

class BoxLayout : Container<ContainerStyle>(::ContainerStyle) {

    override fun getContentSize(): Size {
        var min = Size(0, 0)
        for (child in children) {
            min = min.combineMax(child.getSize())
        }

        return min
    }

    override fun reflowContent(pos: PosXY, padding: PosXY, size: Size) {
        val offset = pos + padding

        for (child in children) {
            child.reflowNode(offset, size)
        }
    }
}