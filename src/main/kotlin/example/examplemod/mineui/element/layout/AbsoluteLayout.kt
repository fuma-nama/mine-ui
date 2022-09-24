package example.examplemod.mineui.element.layout

import example.examplemod.mineui.element.Container
import example.examplemod.mineui.element.ContainerStyle
import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.utils.Size

class AbsoluteStyle : ContainerStyle() {
}

/**
 * Full access to change absolute position of element
 *
 * Always showed as top element
 */
class AbsoluteLayout : Container<AbsoluteStyle>(::AbsoluteStyle) {
    override fun getSize() = Size(0, 0)
    override fun getContentSize() = Size(0, 0)

    override fun reflowContent(pos: PosXY, padding: PosXY, size: Size) {
        for (child in children) {
            child.reflowNode(absolutePosition, child.getSize())
        }
    }
}