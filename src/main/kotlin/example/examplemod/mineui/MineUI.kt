package example.examplemod.mineui

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font

fun interface Drawer {
    fun DrawContext.draw()
}

class DefaultDrawer: Drawer {
    override fun DrawContext.draw() {
        for (child in rendered.children.values) {
            child.draw(stack)
        }
    }
}

fun component(render: RenderContext.() -> Unit): UI {
    return UI(RenderContext().apply(render))
}

class UI(
    var context: RenderContext
) {
    fun draw(stack: PoseStack) {
        context.draw(stack)
    }
}

data class HookKey(
    val type: String, val id: String
)

class DrawContext(
    val stack: PoseStack,
    val rendered: RenderContext
) {
    var font: Font = Minecraft.getInstance().font
}

class RenderContext(
    var drawer: Drawer = DefaultDrawer()
) {
    val states = hashMapOf<String, Any?>()
    val hooks = hashMapOf<HookKey, Any?>()
    val children = hashMapOf<Any, RenderContext>()

    fun child(key: Any, scope: RenderContext.() -> Unit) {
        add(key, RenderContext().apply(scope))
    }

    fun add(key: Any, element: RenderContext) {
        children[key] = element
    }

    fun draw(stack: PoseStack) {
        val drawer = this.drawer

        if (drawer != null) {
            with (drawer) {
                DrawContext(stack, this@RenderContext).draw()
            }
        }
    }
}