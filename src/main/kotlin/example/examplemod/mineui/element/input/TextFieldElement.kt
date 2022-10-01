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
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.font.TextFieldHelper
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Style
import java.awt.Color

class TextFieldStyle : BoxStyle(), LabelBuilder {
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

    init {
        color = Color.WHITE
        background = Color.BLACK
        border(1, 1, Color.DARK_GRAY)
        padding(4)
    }
}

data class Cursor(
    val step: Int = 30,
    val color: Color = Color.WHITE,
    val width: Int = 1,
    val selection: Color = Color.BLUE,
)

class TextFieldElement : BoxElement<TextFieldStyle>(::TextFieldStyle) {
    private val splitter get() = style.font.splitter
    private val value get() = style.value
    val helper = FieldHelperImpl(
        { value },
        { style.onChange(it) }
    )

    var tick by helper::tick
    val cursor by helper::cursorPos
    var selection by helper::selectionPos

    override fun onClick(x: Double, y: Double, mouseButton: Int, context: GuiEventContext) {
        val ox = x - (contentOffset.x + absolutePosition.x)
        val index = splitter.plainIndexAtWidth(value, ox.toInt(), style.textStyle).coerceIn(0, value.length)
        helper.setCursorPos(index, Screen.hasShiftDown())

        super.onClick(x, y, mouseButton, context)
    }

    override fun onKeyPressed(key: Int, p_94746_: Int, p_94747_: Int, context: GuiEventContext) {
        if (helper.keyPressed(key)) {
            context.prevent = true
            context.reflow = true
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
        val ox = mouseX - (contentOffset.x + absolutePosition.x)
        val index = splitter.plainIndexAtWidth(value, ox.toInt(), style.textStyle).coerceIn(0, value.length)

        selection = index
        super.onDrag(mouseX, mouseY, mouseButton, dragX, dragY, context)
    }

    override fun onType(char: Char, key: Int, context: GuiEventContext) {
        if (helper.charTyped(char)) {
            context.prevent = true
            context.reflow = true
        }

        super.onType(char, key, context)
    }

    override fun getContentSize(): Size {
        return Size(
            style.font.width(style.value),
            style.font.lineHeight
        )
    }

    fun indexToPosition(index: Int): Int {
        return style.font.width(value.substring(0, index.coerceIn(0, value.length)))
    }

    override fun drawContent(stack: DrawStack, size: Size) {
        stack.scissor(0, 0, size.width, size.height)
        val focus = ui.state.focus == this
        val cursorPos = indexToPosition(cursor)
        val cursorSpace = style.cursor.width + 5

        if (value.isNotEmpty()) {
            if (cursorPos + cursorSpace > size.width) {
                stack.translate(x = - (cursorPos + cursorSpace - size.width))
            }

            stack.drawText(style.font, value, 0F, 0F, style.color)

            if (helper.isSelecting) {
                val selectionPos = indexToPosition(selection)
                val left = cursorPos.coerceAtMost(selectionPos)
                val right = cursorPos.coerceAtLeast(selectionPos)

                stack.drawHighlight(left, 0, right - left, style.font.lineHeight, style.cursor.selection.rgb)
            }
        } else {
            stack.drawText(style.font, style.placeholder, 0F, 0F, style.placeholderColor)
        }

        if (tick / style.cursor.step % 2 == 0 && focus) {
            stack.fillRect(cursorPos, 0, style.cursor.width, style.font.lineHeight, style.cursor.color)
        }
        helper.updateTick()
        RenderSystem.disableScissor()
    }
}

class FieldHelperImpl(v: () -> String, setV: (String) -> Unit, validator: (String) -> Boolean = { true }) : TextFieldHelper(
    v,
    setV,
    createClipboardGetter(Minecraft.getInstance()),
    createClipboardSetter(Minecraft.getInstance()),
    validator
) {
    var tick: Int = 0
    var lastCursor: Int? = null

    fun updateTick() {
        tick = if (cursorPos != lastCursor || tick == Int.MAX_VALUE) {
            lastCursor = cursorPos
            0
        } else {
            tick + 1
        }
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
    RenderSystem.enableColorLogicOp()
    RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE)
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
}