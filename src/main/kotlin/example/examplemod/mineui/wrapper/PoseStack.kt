package example.examplemod.mineui.wrapper

import com.mojang.blaze3d.vertex.PoseStack
import example.examplemod.mineui.style.Point4
import net.minecraft.client.gui.Gui

fun PoseStack.drawBorder(x: Int, y: Int, width: Int, height: Int, color: Int, thickness: Point4) {
    val right = (x + width).coerceAtLeast(x)
    val bottom = (y + height).coerceAtLeast(y)

    fun line(
        x: Int, y: Int,
        width: Int, height: Int
    ) {
        Gui.fill(this, x, y, x + width, y + height, color)
    }

    line(x + thickness.left, y, width - thickness.px, thickness.top)
    line(right - thickness.right, y, thickness.right, height)
    line(x + thickness.left, bottom - thickness.bottom, width - thickness.px, thickness.bottom)
    line(x, y, thickness.left, height)
}