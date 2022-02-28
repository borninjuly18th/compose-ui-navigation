package ru.bezgrebelnygrigory.composeuinavigation.entity

import android.os.Parcelable
import androidx.compose.runtime.Composable
import ru.bezgrebelnygrigory.composeuinavigation.NavigationController

interface NavigationScreen : NavigationItem, Parcelable {

    /** Ключ/Scope данного экрана для view model и т.п. */
    val key: String

    @Composable
    fun Draw(controller: NavigationController)
}
