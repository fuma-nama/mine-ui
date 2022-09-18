package example.examplemod.mineui

import com.mojang.blaze3d.vertex.PoseStack
import example.examplemod.mineui.context.DrawContext
import example.examplemod.mineui.drawer.DefaultDrawer
import example.examplemod.mineui.drawer.Drawer
import example.examplemod.mineui.hooks.Context

@DslMarker
annotation class DslBuilder

fun component(render: RenderContext.() -> Unit): UI {
    return UI(render)
}

data class HookKey(
    val type: String, val id: Any
)

@DslBuilder
class RenderContext(
    var ui: UI,
    val render: RenderContext.() -> Unit,
    var contexts: Map<Context<*>, Any?> = hashMapOf()
) {
    val hooks: HashMap<HookKey, Any?> = hashMapOf()
    var drawer: Drawer = DefaultDrawer(this)
        set(v) {
            v.rendered = this
            field = v
        }

    val children = linkedMapOf<Any, RenderContext>()
    private var snapshot: Map<Any, RenderContext>? = null

    fun update() {
        render()
    }

    fun render(): RenderContext {
        //create snapshot
        snapshot = HashMap(children)
        children.clear()

        //re-render children
        render(this)

        //clear snapshot
        snapshot = null
        return this
    }

    inline fun<reified T> use(key: HookKey): T? {
        return hooks[key] as T?
    }

    inline fun<reified T> use(key: HookKey, initial: () -> T): T {
        return hooks.getOrPut(key, initial) as T
    }

    fun<V> Context<V>.provider(
        value: V,
        render: RenderContext.() -> Unit
    ) {
        val prev = this@RenderContext.contexts
        contexts = HashMap(prev).also {
            it[this] = value
        }

        render(this@RenderContext)
        contexts = prev
    }

    fun child(key: Any, render: RenderContext.() -> Unit) {
        val context = snapshot?.get(key)?: RenderContext(ui, render, contexts)

        add(key, context)
    }

    /**
     * Add and Render given Context
     */
    fun add(key: Any, element: RenderContext) {
        children[key] = element.render()
    }

    fun draw(stack: PoseStack, mouseX: Int, mouseY: Int) {
        with (drawer) {
            DrawContext(
                stack, mouseX, mouseY
            ).draw()
        }
    }
}