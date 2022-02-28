package ru.bezgrebelnygrigory.composeuinavigation.viewmodel

abstract class ViewModelHolder<VM> {

    // scope key, dependency key and instance
    private val dependencies = hashMapOf<String, HashMap<String, VM>>()

    fun getScopeOrCreate(scope: String): HashMap<String, VM> =
        dependencies.getOrPut(scope, ::hashMapOf)

    fun getInstance(scope: HashMap<String, VM>, key: String): VM? = scope[key]

    fun setInstance(scope: String, key: String, instance: VM) {
        dependencies[scope]?.set(key, instance)
    }

    inline fun <reified T : VM> get(scope: String, key: String, factory: () -> T): T {
        val scopeList = getScopeOrCreate(scope)
        return when (val dependency = getInstance(scopeList, key)) {
            null -> {
                factory.invoke().also {
                    setInstance(scope, key, it)
                }
            }
            is T -> dependency
            else -> throw IllegalArgumentException("Try get class instance ${T::class.java.name}, but it was it ${dependency!!::class.java.name}")
        }
    }

    fun clear() {
        dependencies.forEach { (_, scope) ->
            scope.forEach { (_, viewModel) ->
                onViewModelCleared(viewModel)
            }
        }
        dependencies.clear()
    }

    fun clearScope(scopeKey: String) {
        dependencies[scopeKey]?.forEach { (_, viewModel) ->
            onViewModelCleared(viewModel)
        }
        dependencies.remove(scopeKey)
    }

    fun removeInstance(scopeKey: String, key: String) {
        dependencies[scopeKey]?.get(key)?.let(::onViewModelCleared)
        dependencies[scopeKey]?.remove(key)
    }

    abstract fun onViewModelCleared(viewModel: VM)
}
