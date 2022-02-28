package ru.bezgrebelnygrigory.composeuinavigation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun getActivity(): Activity? {
    fun tryFindAppDependencies(context: Context): Activity? {
        return when (context) {
            is Activity -> context
            is ContextWrapper -> tryFindAppDependencies(context.baseContext)
            else -> null
        }
    }
    val context = LocalContext.current
    return tryFindAppDependencies(context)
}
