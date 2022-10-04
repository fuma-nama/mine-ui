package example.examplemod.mineui.element.input

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexFormat
import example.examplemod.mineui.core.GuiEventContext
import example.examplemod.mineui.element.BoxElement
import example.examplemod.mineui.element.BoxStyle
import example.examplemod.mineui.element.LabelBuilder
import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.utils.edit
import example.examplemod.mineui.wrapper.DrawStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Style
import java.awt.Color

data class Cursor(
    val step: Int = 30,
    val color: Color = Color.CYAN,
    val width: Int = 1,
    val selection: Color = Color.CYAN,
    /**
     * Apply color reverse filter on selection
     */
    val reverseColor: Boolean = false
)

open class TextInputStyle : BoxStyle(), LabelBuilder {
    override var textStyle: Style = Style.EMPTY
    override var color: Color = Color.WHITE
    override var font: Font = Minecraft.getInstance().font
    var placeholder: String = ""
    var placeholderColor: Color = Color.LIGHT_GRAY
    var cursor: Cursor = Cursor()
    var value: String = ""
    var onChange: (String) -> Unit = {}

    fun placeholder(text: String = placeholder, color: Color = placeholderColor) {
        placeholder = text
        placeholderColor = color
    }

    fun cursor(
        step: Int = 30,
        color: Color = Color.CYAN,
        width: Int = 1,
        selection: Color = Color.CYAN.edit(alpha = 100),
        reverseColor: Boolean = false
    ) {
        this.cursor = Cursor(step, color, width, selection, reverseColor)
    }

    init {
        requireFocus = true
    }
}

abstract class TextInput<S: TextInputStyle>(create: () -> S)  : BoxElement<S>(create) {
    abstract val helper: FieldHelperImpl

    val tick get() = helper.tick
    val cursor get() = helper.cursorPos
    val selection get() = helper.selectionPos

    override fun onClick(x: Double, y: Double, mouseButton: Int, context: GuiEventContext) {
        val index = positionToIndex(x, y)
        helper.setCursorPos(index, Screen.hasShiftDown())

        super.onClick(x, y, mouseButton, context)
    }

    override fun onKeyPressed(key: Int, p_94746_: Int, p_94747_: Int, context: GuiEventContext) {
        if (helper.keyPressed(key)) {
            context.prevent()
            context.requireReflow()
        }

        super.onKeyPressed(key, p_94746_, p_94747_, context)
    }

    override fun onDrag(
        mouseX: Double,
        mouseY: Double,
        mouseButton: Int,
        dragX: Double,
        dragY: Double,
        context: GuiEventContext
    ) {
        helper.selectionPos = positionToIndex(mouseX, mouseY)
    }

    override fun onType(char: Char, key: Int, context: GuiEventContext) {
        if (helper.charTyped(char)) {
            context.prevent()
            context.requireReflow()
        }
    }

    override fun getContentSize(): Size {
        return Size(
            style.font.width(style.value),
            style.font.lineHeight
        )
    }

    abstract fun indexToPosition(index: Int): PosXY
    abstract fun positionToIndex(mouseX: Double, mouseY: Double): Int

    override fun drawContent(stack: DrawStack, size: Size) {
        stack.scissor(0, 0, size.width, size.height)
        drawInput(stack, size, ui.state.focus == this)

        helper.updateTick()
        RenderSystem.disableScissor()
    }

    abstract fun drawInput(stack: DrawStack, size: Size, focus: Boolean)

    fun DrawStack.drawHighlight(x: Int, y: Int, width: Int, height: Int) {
        val cursor = style.cursor
        if (cursor.reverseColor) {
            RenderSystem.enableColorLogicOp()
            RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE)
        }

        drawHighlight(x, y, width, height, cursor.selection.rgb)
    }
}

fun DrawStack.drawHighlight(x: Int, y: Int, width: Int, height: Int, color: Int) {
    val a = (color shr 24 and 255).toFloat() / 255.0f
    val r = (color shr 16 and 255).toFloat() / 255.0f
    val g = (color shr 8 and 255).toFloat() / 255.0f
    val b = (color and 255).toFloat() / 255.0f

    val tesselator = Tesselator.getInstance()
    val bufferbuilder = tesselator.builder
    RenderSystem.setShader { GameRenderer.getPositionShader() }
    RenderSystem.setShaderColor(r, g, b, a)
    RenderSystem.disableTexture()

    with (bufferbuilder) {
        begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION)

        val i = translated.x + x
        val j = translated.y + y
        val k = i + width
        val l = j + height
        vertex(i.toDouble(), l.toDouble(), 0.0).endVertex()
        vertex(k.toDouble(), l.toDouble(), 0.0).endVertex()
        vertex(k.toDouble(), j.toDouble(), 0.0).endVertex()
        vertex(i.toDouble(), j.toDouble(), 0.0).endVertex()

        tesselator.end()
    }
    RenderSystem.disableColorLogicOp()
    RenderSystem.disableTexture()
    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
}