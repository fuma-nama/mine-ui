package example.examplemod.mineui.style

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Style
import java.awt.Color

class LabelStyle {
    var color: Color = Color.WHITE
    var font: Font = Minecraft.getInstance().font

    var fontSize: Float = 1F
    var fontStyle: Array<FontStyle> = emptyArray()
}

enum class FontStyle(val style: Style) {
    Bold(Style.EMPTY.withBold(true)),
    Underline(Style.EMPTY.withUnderlined(true)),
    Italic(Style.EMPTY.withItalic(true)),
    Obfuscated(Style.EMPTY.withObfuscated(true)),
}