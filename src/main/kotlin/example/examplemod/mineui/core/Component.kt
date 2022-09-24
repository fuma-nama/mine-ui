package example.examplemod.mineui.core

import example.examplemod.mineui.DslBuilder
import example.examplemod.mineui.element.Container
import example.examplemod.mineui.element.StyleContext
import example.examplemod.mineui.element.UIElement
import example.examplemod.mineui.hooks.Context
import example.examplemod.mineui.utils.extractChildren
import example.examplemod.mineui.wrapper.DrawStack

@DslBuilder
open class Component(
    var ui: UI,
    val render: Component.() -> Unit,
    var contexts: Map<Context<*>, Any?> = mapOf(),
    var element: UIElement<*>? = null
) {
    val hooks: HashMap<HookKey, Any?> = hashMapOf()
    val children = linkedMapOf<Any, Component>()

    private var snapshot: Map<Any, Component>? = null

    inline fun<reified E: UIElement<S>, S: StyleContext> element(element: () -> E, noinline style: S.() -> Unit): E {
        if (this.element == null) {
            this.element = element()
        }

        val current = this.element as E
        current.update(style)
        return current
    }

    /**
     * We need to reflow all elements from root
     *
     * since we don't know how many elements will be affected in the update
     */
    fun update() {
        render()
        ui.reflow()
    }

    fun render(): Component {
        //create snapshot
        snapshot = HashMap(children)

        children.clear()
        element?.invalidate()

        //re-render children
        render(this)

        //clear snapshot
        snapshot = null
        return this
    }

    fun<V> Context<V>.provider(
        value: V,
        render: Component.() -> Unit
    ) {
        val prev = this@Component.contexts
        contexts = HashMap(prev).also {
            it[this] = value
        }

        render(this@Component)
        contexts = prev
    }

    fun child(key: Any, render: Component.() -> Unit) {
        val context = snapshot?.get(key)?: Component(ui, render, contexts)
        add(key, context)
    }

    /**
     * Add and Render given Context
     */
    fun add(key: Any, comp: Component) {
        children[key] = comp.render()

        val self = this.element

        if (self is Container<*>) {
            self.children += extractChildren(comp)
        }
    }

    fun draw(stack: DrawStack) {
        val element = this.element?: error("Missing UI element linked to component")

        element.drawNode(stack)
    }

    inline fun<reified T> use(key: HookKey): T? {
        return hooks[key] as T?
    }

    inline fun<reified T> use(key: HookKey, initial: () -> T): T {
        return hooks.getOrPut(key, initial) as T
    }
}