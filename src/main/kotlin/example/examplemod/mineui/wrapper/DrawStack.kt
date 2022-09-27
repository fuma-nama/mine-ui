package example.examplemod.mineui.wrapper

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import example.examplemod.mineui.style.PosXY
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.Gui
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import java.awt.Color

open class DrawStackDefault(val base: PoseStack): DrawStack {
    override var translated: PosXY = PosXY(0, 0)

    override fun drawText(font: Font, text: String, x: Float, y: Float, color: Int) {
        font.draw(base, text, translated.x + x, translated.y + y, color)
    }

    override fun drawText(font: Font, text: Component, x: Float, y: Float, color: Int) {
        font.draw(base, text, translated.x + x, translated.y + y, color)
    }

    override fun drawImage(x: Int, y: Int, offsetX: Int, offsetY: Int, width: Int, height: Int, containerW: Int, containerH: Int, image: ResourceLocation) {
        val texture = Minecraft.getInstance().textureManager.getTexture(image)
        texture.bind()
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, image)

        Gui.blit(base,
            translated.x + x,
            translated.y + y,
            0, //z offset
            offsetX.toFloat(),
            offsetY.toFloat(),
            width, height,
            containerW, containerH
        )
    }

    override fun scissor(x: Int, y: Int, width: Int, height: Int) {
        val dx = translated.x + x
        val dy = translated.y + y

        Gui.enableScissor(
            dx, dy, //x and y
            dx + width, dy + height //size
        )
    }

    override fun scale(x: Float, y: Float, z: Float) {
        base.scale(x, y, z)
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

    fun drawText(font: Font, text: String, x: Float, y: Float, color: Color) =
        drawText(font, text, x, y, color.rgb)

    fun drawText(font: Font, text: String, x: Float, y: Float, color: Int)

    fun drawText(font: Font, text: Component, x: Float, y: Float, color: Color) =
        drawText(font, text, x, y, color.rgb)

    fun drawText(font: Font, text: Component, x: Float, y: Float, color: Int)

    fun fillRect(x: Int, y: Int, width: Int, height: Int, color: Color) =
        fillRect(x, y, width, height, color.rgb)

    fun fillRect(x: Int, y: Int, width: Int, height: Int, color: Int)

    fun drawImage(x: Int, y: Int, width: Int, height: Int, image: ResourceLocation) =
        drawImage(x, y, 0, 0, width, height, width, height, image)

    fun drawImage(x: Int, y: Int, offsetX: Int, offsetY: Int, width: Int, height: Int, containerW: Int, containerH: Int, image: ResourceLocation)

    fun scissor(x: Int, y: Int, width: Int, height: Int)

    fun scale(x: Float, y: Float, z: Float)
}

fun DrawStack.translate(render: () -> Unit) {
    val before = translated
    render()

    translated = before
}