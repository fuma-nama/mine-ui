package example.examplemod.mineui.wrapper

import com.mojang.blaze3d.vertex.PoseStack
import example.examplemod.mineui.PosXY
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.Gui
import net.minecraft.network.chat.Component
import java.awt.Color

open class DrawStackDefault(val base: PoseStack): DrawStack {
    override var translated: PosXY = PosXY(0, 0)
    set(v) {

        if (v.x < 0) {
            error("It is less than 0! $v")
        }

        field = v
    }

    override fun drawText(font: Font, text: String, x: Float, y: Float, color: Int) {
        font.draw(base, text, translated.x + x, translated.y + y, color)
    }

    override fun drawText(font: Font, text: Component, x: Float, y: Float, color: Int) {
        font.draw(base, text, translated.x + x, translated.y + y, color)
    }

    override fun fillRect(x: Int, y: Int, width: Int, height: Int, color: Int) {
        val ox = translated.x + x
        val oy = translated.y + y

        Gui.fill(base, ox, oy, ox + width, oy + height, color)
    }
}

interface DrawStack {
    var translated: PosXY
    fun translate(x: Int = 0, y: Int = 0)  {
        translated = PosXY(translated.x + x, translated.y + y)
    }
    fun translate(pos: PosXY) = translate(pos.x, pos.y)

    fun drawText(font: Font, text: String, x: Float, y: Float, color: Color) =
        drawText(font, text, x, y, color.rgb)

    fun drawText(font: Font, text: String, x: Float, y: Float, color: Int)

    fun drawText(font: Font, text: Component, x: Float, y: Float, color: Color) =
        drawText(font, text, x, y, color.rgb)

    fun drawText(font: Font, text: Component, x: Float, y: Float, color: Int)

    fun fillRect(x: Int, y: Int, width: Int, height: Int, color: Color) =
        fillRect(x, y, width, height, color.rgb)

    fun fillRect(x: Int, y: Int, width: Int, height: Int, color: Int)
}

fun DrawStack.translate(render: () -> Unit) {
    val before = translated
    render()

    translated = before
}