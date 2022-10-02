package example.examplemod.mineui.element.input

import example.examplemod.mineui.core.GuiEventContext
import example.examplemod.mineui.style.PosXY
import example.examplemod.mineui.utils.Size
import example.examplemod.mineui.wrapper.DrawStack
import example.examplemod.mineui.wrapper.drawTextLines
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Style
import org.lwjgl.glfw.GLFW
import java.awt.Color

class TextAreaStyle : TextInputStyle() {
    init {
        color = Color.WHITE
        background = Color.BLACK
        border(1, 1, Color.DARK_GRAY)
        padding(4)
    }
}

data class LineData(
    val y: Int,
    val start: Int, val end: Int,
)

class TextAreaElement : TextInput<TextAreaStyle>(::TextAreaStyle) {
    private val value get() = style.value
    lateinit var lines: List<LineData>
    private val splitter get() = style.font.splitter
    override val helper = FieldHelperImpl(
        { style.value },
        { style.onChange(it) }
    )

    override fun onKeyPressed(key: Int, p_94746_: Int, p_94747_: Int, context: GuiEventContext) {
        val prevent = when (key) {
            GLFW.GLFW_KEY_ENTER -> {
                helper.insertText("\n")
                true
            }
            GLFW.GLFW_KEY_UP -> {
                changeLine(-1)
                true
            }
            GLFW.GLFW_KEY_DOWN -> {
                changeLine(1)
                true
            }
            else -> false
        }

        if (prevent) {
            context.prevent = prevent
        }

        super.onKeyPressed(key, p_94746_, p_94747_, context)
    }

    fun changeLine(add: Int) {
        var currentIndex = getLineIndex(cursor)
        if (currentIndex == -1) return

        val atLineBreak = value.getOrNull(this.cursor - 1) == '\n'

        if (atLineBreak) {
            currentIndex++
        }

        val index = (currentIndex + add).coerceIn(0, lines.lastIndex)
        val next = lines[index]

        when {
            currentIndex + add > lines.lastIndex -> {
                helper.setCursorPos(value.length, Screen.hasShiftDown())
            }
            atLineBreak -> {
                helper.setCursorPos(next.start, Screen.hasShiftDown())
            }
            else -> {
                val offset = (cursor - lines[currentIndex].start)
                val end = if (next.value.endsWith('\n')) next.end - 1 else next.end

                helper.setCursorPos((next.start + offset).coerceAtMost(end), Screen.hasShiftDown())
            }
        }
    }

    override fun indexToPosition(index: Int): PosXY {
        val data = getLine(index)?: return PosXY(0, 0)

        return indexToPosition(data, index)
    }

    fun indexToPosition(line: LineData, index: Int, excludeLineBreak: Boolean = true): PosXY {
        return PosXY(
            style.font.width(
                value.substring(line.start, index.coerceIn(0, line.end)).let {
                    if (excludeLineBreak) it.trimEnd('\n')
                    else it
                }
            ),
            line.y
        )
    }

    override fun reflow(pos: PosXY, size: Size) {
        super.reflow(pos, size)

        val lines = arrayListOf<LineData>()
        splitter.splitLines(value,
            size.width,
            Style.EMPTY,
            true
        ) { _: Style, start: Int, end: Int ->
            lines += LineData(
                lines.size * this.style.font.lineHeight,
                start, end
            )
        }

        this.lines = lines
    }

    override fun positionToIndex(mouseX: Double, mouseY: Double): Int {
        val ox = mouseX - (contentOffset.x + absolutePosition.x)
        val oy = mouseY - (contentOffset.y + absolutePosition.y)
        val line = lines.findLast {
            oy.toInt() in it.y..(it.y + style.font.lineHeight)
        }?: return value.length

        return line.start + splitter.plainIndexAtWidth(line.trimmed, ox.toInt(), style.textStyle)
    }

    fun getCursorPosition(): PosXY {
        val atLineBreak = value.getOrNull(this.cursor - 1) == '\n'
        val original = indexToPosition(cursor)

        return if (atLineBreak) {
            PosXY(0, original.y + style.font.lineHeight)
        } else {
            original
        }
    }

    override fun drawInput(stack: DrawStack, size: Size, focus: Boolean) {
        val cursor = getCursorPosition()
        val cursorLine = getActualLine(this.cursor)
        val cursorSpace = style.font.lineHeight * 3

        if (value.isNotEmpty()) {
            if (cursor.y + cursorSpace > size.height) {
                stack.translate(y = - (cursor.y + cursorSpace - size.height))
            }

            val selectionLine = getActualLine(this.selection)

            val (selectStart, selectEnd) = IndexXY.leftRight(
                IndexXY(this.cursor, cursorLine),
                IndexXY(this.selection, selectionLine)
            )

            for ((i, line) in lines.withIndex()) {
                val text = line.trimmed

                stack.drawText(style.font, text, 0F, line.y.toFloat(), style.color)
                if (helper.isSelecting && i in selectStart.y..selectEnd.y) {
                    val left = when (i) {
                        selectStart.y -> indexToPosition(line, selectStart.x).x
                        else -> 0
                    }
                    val right = when (i) {
                        selectEnd.y -> indexToPosition(line, selectEnd.x).x
                        else -> size.width
                    }

                    stack.drawHighlight(left, line.y, right - left, style.font.lineHeight, style.cursor.selection.rgb)
                }
            }
        } else {
            stack.drawTextLines(style.font, style.placeholder, 0F, 0F, style.placeholderColor.rgb)
        }

        if (tick / style.cursor.step % 2 == 0 && focus) {
            stack.fillRect(cursor.x, cursor.y, style.cursor.width, style.font.lineHeight, style.cursor.color)
        }
    }

    fun getLine(index: Int): LineData? {
        return lines.find {
            index in it.start..it.end
        }
    }

    fun getLineIndex(index: Int): Int {
        return lines.indexOfFirst {
            index in it.start..it.end
        }
    }

    fun getActualLine(index: Int): Int {
        val line = getLineIndex(index)

        return if (value.getOrNull(index - 1) == '\n') {
            line + 1
        } else {
            line
        }
    }

    val LineData.value: String get() {
        return this@TextAreaElement.value.substring(start, end)
    }
    val LineData.trimmed: String get() = this.value.trimEnd('\n')
}

data class IndexXY(val x: Int, val y: Int) {
    fun greaterThan(other: IndexXY): Boolean {
        return if (this.y == other.y) {
            this.x > other.x
        } else {
            this.y > other.y
        }
    }

    companion object {
        fun leftRight(o: IndexXY, o2: IndexXY): Pair<IndexXY, IndexXY> {
            return if (o.greaterThan(o2)) {
                o2 to o
            } else {
                o to o2
            }
        }
    }
}
