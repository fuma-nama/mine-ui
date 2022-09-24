package example.examplemod.mineui.hooks

import example.examplemod.mineui.core.Component
import example.examplemod.mineui.core.HookKey
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

inline fun<reified V> Component.useState(noinline initial: () -> V) = useState(initial::class, initial)

inline fun<reified V> Component.useState(id: Any, crossinline initial: () -> V): State<V> {
    val key = HookKey("useState", id)

    return object : State<V>() {
        override var value: V
            get() = use(key, initial)
            set(value) {
                hooks[key] = value
                this@useState.update()
            }
    }
}

abstract class State<V> {
    abstract var value: V

    fun update(value: V) {
        this.value = value
    }

    operator fun<P> getValue(parent: P, property: KProperty<*>): V {
        return value
    }

    operator fun<P> setValue(parent: P, property: KProperty<*>, value: V) {
        update(value)
    }
}