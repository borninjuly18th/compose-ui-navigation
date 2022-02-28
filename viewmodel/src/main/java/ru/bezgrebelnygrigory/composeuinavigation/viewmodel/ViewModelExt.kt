package ru.bezgrebelnygrigory.composeuinavigation.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
inline fun <reified VM> ViewModelHolder<VM>.viewModel(
    key: String,
    scope: String = key,
    crossinline viewModelFactory: () -> VM = { throw IllegalStateException("No view model factory found!") },
): VM {
    return remember(key1 = key, key2 = scope) {
        get(scope, key) { viewModelFactory.invoke() }
    }
}

@Composable
inline fun <reified VM, S : Any> ViewModelHolder<VM>.viewModel(
    key: String,
    scope: String = key,
    initState: S,
    crossinline onSaveState: (VM) -> S,
    crossinline viewModelFactory: (S) -> VM,
): VM {
    var state by rememberSaveable(null, key = scope + key) {
        mutableStateOf(initState)
    }
    val viewModel = remember(key1 = key, key2 = scope) {
        get(scope, key) { viewModelFactory.invoke(state) }
    }
    SideEffect {
        state = onSaveState.invoke(viewModel)
    }

    return viewModel
}
