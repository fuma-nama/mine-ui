package example.examplemod.mineui.hooks

import example.examplemod.mineui.context.RenderContext

class Context<V> internal constructor(val initial: V)

inline fun<reified V> RenderContext.useContext(context: Context<V>): V {
    return contexts.getOrDefault(context, context.initial) as V
}

fun<V> createContext(): Context<V?> {
    return Context(null)
}

fun<V> createContext(initial: V): Context<V> {
    return Context(initial)
}