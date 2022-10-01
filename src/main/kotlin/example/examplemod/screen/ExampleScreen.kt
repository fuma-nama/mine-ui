package example.examplemod.screen

import com.mojang.blaze3d.vertex.PoseStack
import example.examplemod.ExampleMod
import example.examplemod.mineui.*
import example.examplemod.mineui.core.UI
import example.examplemod.mineui.element.layout.StackLayout
import example.examplemod.mineui.hooks.createContext
import example.examplemod.mineui.hooks.useContext
import example.examplemod.mineui.hooks.useState
import example.examplemod.mineui.style.*
import example.examplemod.mineui.utils.Size
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
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

    override fun mouseReleased(x: Double, y: Double, button: Int): Boolean {
        return root.onMouseReleased(x, y, button)
    }

    override fun keyPressed(key: Int, p_96553_: Int, p_96554_: Int): Boolean {
        val prevent = root.onKeyPress(key, p_96553_, p_96554_)

        return if (prevent) {
            true
        }  else {
            super.keyPressed(key, p_96553_, p_96554_)
        }
    }

    override fun mouseClicked(x: Double, y: Double, type: Int): Boolean {
        return root.onClick(x, y, type)
    }

    override fun charTyped(char: Char, key: Int): Boolean {
        return root.onType(char, key)
    }

    override fun mouseScrolled(p_94686_: Double, p_94687_: Double, p_94688_: Double): Boolean {
        return root.onScroll(p_94686_, p_94687_, p_94688_)
    }

    override fun mouseDragged(
        p_94699_: Double,
        p_94700_: Double,
        p_94701_: Int,
        p_94702_: Double,
        p_94703_: Double
    ): Boolean {
        return root.onDrag(
            p_94699_,
            p_94700_,
            p_94701_,
            p_94702_,
            p_94703_
        )
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

val sky = ResourceLocation(ExampleMod.ID, "sky.jpg")
val meme = ResourceLocation(ExampleMod.ID, "meme.png")

fun example() = component {
    var direction by useState { Direction.Row }
    var toggle by useState { false }

    element(::StackLayout) {
        background = Color.RED
        padding(4)
        backgroundImage = meme
        backgroundFit = ImageFit.Contain
        gap = 5
        overflow(Overflow.Scroll)
    }

    ThemeContext.provider(Theme("Hello World")) {
        test(0)
    }
    if (toggle) {
        image { src = sky; width = 170; height = 100; objectFit = ImageFit.Cover }
    }

    button({
        bold()

        click { x: Double, y: Double, type: Int ->
            direction = when (direction) {
                Direction.Row -> Direction.Column
                Direction.Column -> Direction.Row
            }
        }

        border(5, 5, Color.WHITE)
    }) { "Click Me" }
    label { "Hello MONEY" }
    simpleGrid({ columns(1, 1, 2); spacing(4) }) {
        box({
            background = Color.BLUE
            click { x, y, type ->
                toggle = !toggle
            }
        }) {
            label { "Hello Kane" }
        }
        box({ background = Color.BLUE }) {
            label { if (toggle) "Hello World, I am Gay" else "MONEY" }
        }
        box({ background = Color.GREEN; size(30, 50) }) {}
    }

    stack({ align(horizontal = HorizontalAlign.Fill) }) {
        box({ background = Color.BLUE }) {
            label { "Hello Kane" }
        }
        box({ background = Color.BLUE }) {
            label { if (toggle) "Hello World, I am Gay" else "MONEY" }
        }
        box({ background = Color.GREEN; size(30, 50) }) {}
    }

    mask({
        maskColor = if (toggle) Color(255, 0, 0, 100) else null
    }) {
        column({gap = 5}) {
            box({ background = Color.BLUE }) {
                label { "Hello Kane" }
            }
            box({ background = Color.BLUE }) {
                label { if (toggle) "Hello World, I am Gay" else "MONEY" }
            }
            box({ background = Color.GREEN; size(30, 50) }) {}
        }
    }

    stack({
        size {
             Size(content.width * 2, content.height)
        }
        alignY = VerticalAlign.Center
        padding(10); gap = 5
        background = Color.BLACK
        this.direction = direction
    }) {
        var input by useState { "Text" }
        textField {
            size {
                Size(100, content.height)
            }

            value = input
            onChange = {
                input = it
            }
        }

        label({ bold(); italic() }) { "Hello World" }

        space { Size(10, 10) }
        label { "Hello Kane" }

        absolute({ position(0, 0) }) {
            label { "Wow" }
        }
    }
}