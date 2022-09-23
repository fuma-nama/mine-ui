package example.examplemod.screen

import com.mojang.blaze3d.vertex.PoseStack
import example.examplemod.mineui.*
import example.examplemod.mineui.element.layout.StackLayout
import example.examplemod.mineui.hooks.createContext
import example.examplemod.mineui.hooks.useContext
import example.examplemod.mineui.hooks.useState
import example.examplemod.mineui.style.Align
import example.examplemod.mineui.style.Direction
import example.examplemod.mineui.utils.Size
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import java.awt.Color

class Theme(
    val text: String
)

val ThemeContext = createContext<Theme>()

class ExampleGUI(p_96550_: Component) : Screen(p_96550_) {
    val root: UI = example()

    override fun init() {
        super.init()

        root.updateSize(width, height)
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

fun example() = component {
    var direction by useState { Direction.Row }
    element(::StackLayout) {
        background = Color.RED
        padding(4)
    }


    ThemeContext.provider(Theme("Hello World")) {
        test(0)
    }

    button({
        bold()

        click { x: Double, y: Double, type: Int ->
            direction = when (direction) {
                Direction.Row -> Direction.Column
                Direction.Column -> Direction.Row
            }

            true
        }
    }) { "Click Me" }
    label { "Hello MONEY" }

    stack({
        size {
             Size(content.width * 2, content.height)
        }
        align = Align.Center
        padding(10); gap = 5
        background = Color.BLACK
        this.direction = direction
    }) {
        label({ bold(); italic() }) { "Hello World" }

        label { "Hello Kane" }
        label { "a" }
    }
}