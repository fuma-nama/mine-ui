package example.examplemod.screen

import com.mojang.blaze3d.vertex.PoseStack
import example.examplemod.mineui.component
import example.examplemod.mineui.label
import net.minecraft.network.chat.Component

import net.minecraft.client.gui.screens.Screen

class ExampleGUI(p_96550_: Component?) : Screen(p_96550_) {
    val root = component {
        label { "Hello World" }
    }

    override fun render(stack: PoseStack, a: Int, b: Int, c: Float) {
        super.render(stack, a, b, c)

        root.draw(stack)
    }
}

