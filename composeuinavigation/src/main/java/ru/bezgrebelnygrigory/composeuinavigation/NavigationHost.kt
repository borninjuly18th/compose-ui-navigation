package ru.bezgrebelnygrigory.composeuinavigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.bezgrebelnygrigory.composeuinavigation.entity.NavigationDialog
import ru.bezgrebelnygrigory.composeuinavigation.entity.NavigationScreen
import ru.bezgrebelnygrigory.composeuinavigation.viewmodel.ViewModelHolder

private const val NAVIGATION_SCREENS_KEY = "NavigationHost_navigationScreensKey"
private const val NAVIGATION_DIALOGS_KEY = "NavigationHost_navigationDialogsKey"

@OptIn(ExperimentalComposeUiApi::class)  // DialogProperties, usePlatformDefaultWidth
@Composable
fun <VM> NavigationHost(
    launch: NavigationScreen,
    viewModelHolder: ViewModelHolder<VM>,
) {
    val screensState: MutableState<List<NavigationScreen>> =
        rememberSaveable(key = NAVIGATION_SCREENS_KEY) { mutableStateOf(listOf(launch)) }
    val dialogsState: MutableState<List<NavigationDialog>> =
        rememberSaveable(key = NAVIGATION_DIALOGS_KEY) { mutableStateOf(listOf()) }
    val controller = remember { NavigationController(screensState, dialogsState, viewModelHolder) }
    val activity = getActivity()

    // screen
    val screen by controller.screen.collectAsState()
    screen.Draw(controller)

    // dialog
    val dialog by controller.dialog.collectAsState()
    if (dialog != null) {
        Dialog(
            onDismissRequest = controller::popup,
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            dialog?.Draw(controller)
        }
    }

    BackHandler {
        if (!controller.popup()) {
            viewModelHolder.clear()
            activity?.finish()
        }
    }
}
