package example.examplemod.screen

import com.mojang.blaze3d.vertex.PoseStack
import example.examplemod.mineui.RenderContext
import example.examplemod.mineui.component
import example.examplemod.mineui.drawer.Stack
import example.examplemod.mineui.hooks.Context
import example.examplemod.mineui.hooks.createContext
import example.examplemod.mineui.hooks.useContext
import example.examplemod.mineui.hooks.useState
import example.examplemod.mineui.label
import example.examplemod.mineui.stack
import net.minecraft.network.chat.Component

import net.minecraft.client.gui.screens.Screen
import java.awt.Color

class Theme(
    val text: String
)

val ThemeContext = createContext<Theme>()

class ExampleGUI(p_96550_: Component?) : Screen(p_96550_) {
    val root = component {
        ThemeContext.provider(Theme("Hello World")) {
            test(0)
        }

        stack({padding(10); gap = 5; background = Color.BLACK}) {
            label { "Hello World" }
            label { "Hello Kane" }
            label { "Hello MONEY" }
        }
    }

    override fun mouseClicked(x: Double, y: Double, type: Int): Boolean {
        return root.onClick(x, y, type)
    }

    override fun render(stack: PoseStack, x: Int, y: Int, c: Float) {
        super.render(stack, x, y, c)
        root.draw(stack, x, y)
    }
}

fun RenderContext.test(key: Any) = child(key) {
    val state by useState { "first" }
    val theme = useContext(ThemeContext)

    println("$key $state ${theme?.text}")
}