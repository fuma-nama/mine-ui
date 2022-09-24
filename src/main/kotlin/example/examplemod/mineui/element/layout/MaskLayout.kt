package example.examplemod.mineui.element.layout

import example.examplemod.mineui.element.Container
import example.examplemod.mineui.element.ContainerStyle
import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStack
import example.examplemod.mineui.wrapper.translate
import java.awt.Color

class MaskLayoutStyle : ContainerStyle() {
    var maskColor: Color? = Color(0, 0, 0, 100)
}

class MaskLayout: Container<MaskLayoutStyle>(::MaskLayoutStyle) {
    override fun getContentSize(): Size {
        var size = Size.Empty

        for (child in children) {
            size = size.combineMax(child.getSize())
        }

        return size
    }

    override fun drawFilter(stack: DrawStack, size: Size) {
        val mask = style.maskColor

        if (mask != null) {
            stack.translate {
                stack.translated = absolutePosition
                stack.fillRect(0, 0, size.width, size.height, mask)
            }
        }
    }

    override fun reflowContent(pos: PosXY, padding: PosXY, size: Size) {
        val offset = pos + padding

        for (child in children) {
            child.reflowNode(offset, size)
        }
    }
}