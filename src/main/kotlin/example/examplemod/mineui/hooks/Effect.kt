package example.examplemod.mineui.hooks

import example.examplemod.mineui.HookKey
import example.examplemod.mineui.core.Component
import kotlin.reflect.KProperty

class MemoHook<V>(
    val dependencies: Array<*>,
    val value: V
)

fun<V> Component.useMemo(vararg dependencies: Any?, call: () -> V): V {
    val key = HookKey("useEffect", call::class)
    val cache = use<MemoHook<V>>(key)

    val updated = if (cache != null && cache.dependencies.contentEquals(dependencies)) {
        cache
    } else {
        MemoHook(dependencies, call())
    }
    hooks[key] = updated
    return updated.value
}

fun Component.useEffect(vararg dependencies: Any?, call: () -> Unit) {
    val key = HookKey("useEffect", call::class)
    val cache = use<Array<Any?>>(key)

    if (!cache.contentEquals(dependencies)) {
        call()
    }

    hooks[key] = dependencies
}

inline fun<reified V> Component.useState(noinline initial: () -> V) = useState(initial::class, initial())

inline fun<reified V> Component.useState(id: Any, initial: V): State<V> {
    val key = HookKey("useState", id)
    val value = use(key) { initial }

    return State(value, this)
}

class State<V>(var value: V, val context: Component) {
    fun update(value: V) {
        this.value = value
        context.update()
    }

    operator fun component1() = value
    operator fun component2() = ::update

    operator fun<P> getValue(parent: P, property: KProperty<*>): V {
        return value
    }

    operator fun<P> setValue(parent: P, property: KProperty<*>, value: V) {
        update(value)
    }
}