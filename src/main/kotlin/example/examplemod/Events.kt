package example.examplemod

import example.examplemod.screen.ExampleGUI
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraftforge.event.TickEvent

import net.minecraftforge.eventbus.api.SubscribeEvent
import org.lwjgl.glfw.GLFW

object Events {
    val open = KeyMapping("key.hud.desc", GLFW.GLFW_KEY_N, "key.magicbeans.category")

    @SubscribeEvent
    fun onClientTick(e: TickEvent.ClientTickEvent) {

        if (open.isDown && e.phase == TickEvent.Phase.START) {
            println("Open Screen")
            Minecraft.getInstance().setScreen(ExampleGUI(Component.empty()))
        }
    }
}