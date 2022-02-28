package ru.bezgrebelnygrigory.composeuinavigation

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.bezgrebelnygrigory.composeuinavigation.actions.AddAction
import ru.bezgrebelnygrigory.composeuinavigation.actions.NavigationAction
import ru.bezgrebelnygrigory.composeuinavigation.entity.NavigationDialog
import ru.bezgrebelnygrigory.composeuinavigation.entity.NavigationScreen
import ru.bezgrebelnygrigory.composeuinavigation.viewmodel.ViewModelHolder

class NavigationController(
    private val screensState: MutableState<List<NavigationScreen>>,
    private val dialogsState: MutableState<List<NavigationDialog>>,
    private val viewModelHolder: ViewModelHolder<*>,
) {
    private val screens: List<NavigationScreen> get() = screensState.value
    private val _screen: MutableStateFlow<NavigationScreen> = MutableStateFlow(screens.last())
    val screen: StateFlow<NavigationScreen> get() = _screen

    private val dialogs: List<NavigationDialog> get() = dialogsState.value
    private val _dialog: MutableStateFlow<NavigationDialog?> = MutableStateFlow(dialogs.lastOrNull())
    val dialog: StateFlow<NavigationDialog?> get() = _dialog

    fun send(screen: NavigationScreen, action: NavigationAction = AddAction()) {
        val list = action.execute(screens, screen)

        // удаление view models для которых были удалены экраны
        val diff = screensState.value - list.toSet()
        diff.forEach { viewModelHolder.clearScope(it.key) }

        screensState.value = list
        _screen.value = list.last()
    }

    fun send(dialog: NavigationDialog, action: NavigationAction = AddAction()) {
        val list = action.execute(dialogs, dialog)
        dialogsState.value = list
        _dialog.value = list.lastOrNull()
    }

    fun popup(): Boolean = when {
        dialogs.isNotEmpty() -> {
            dialogsState.value = dialogs.toMutableList().apply { removeLast() }
            _dialog.value = dialogs.lastOrNull()
            true
        }
        screens.size > 1 -> {
            val lastScreen = screens.last()
            viewModelHolder.clearScope(lastScreen.key)
            screensState.value = screens.toMutableList().apply { removeLast() }
            _screen.value = screens.last()
            true
        }
        else -> false
    }
}
