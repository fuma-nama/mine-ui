package example.examplemod.screen

import com.mojang.blaze3d.vertex.PoseStack
import example.examplemod.mineui.*
import example.examplemod.mineui.element.Align
import example.examplemod.mineui.hooks.createContext
import example.examplemod.mineui.hooks.useContext
import example.examplemod.mineui.hooks.useState
import example.examplemod.mineui.utils.Size
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import java.awt.Color

class Theme(
    val text: String
)

val ThemeContext = createContext<Theme>()

var a: Int = 0

class ExampleGUI(p_96550_: Component) : Screen(p_96550_) {
    lateinit var root: UI

    override fun init() {
        super.init()
        if (a > 3) {
            a = 0
        }
        a++

        root = example(width, height)
    }

    override fun mouseClicked(x: Double, y: Double, type: Int): Boolean {
        return root.onClick(x, y, type)
    }

    override fun render(stack: PoseStack, x: Int, y: Int, c: Float) {
        super.render(stack, x, y, c)
        root.draw(stack, x, y)
    }
}

fun example.examplemod.mineui.core.Component.test(key: Any) = child(key) {
    val state by useState { "first" }
    val theme = useContext(ThemeContext)

    println("$key $state ${theme?.text}")
}

fun example(width: Int, height: Int) = component(width, height) {
    ThemeContext.provider(Theme("Hello World")) {
        test(0)
    }

    button({ bold() }) { "Click Me" }
    stack({
        size {
             Size(content.width * 2, content.height)
        }
        align = when (a) {
            1 -> Align.Start
            2 -> Align.Center
            else -> Align.End
        }
        padding(10); gap = 5
        background = Color.BLACK
    }) {
        label({
            bold()
            italic()
        }) { "Hello World" }

        label { "Hello Kane" }
        label { "Hello MONEY" }
    }
}