package ru.bezgrebelnygrigory.composeuinavigation.entity

import android.os.Parcelable
import androidx.compose.runtime.Composable
import ru.bezgrebelnygrigory.composeuinavigation.NavigationController

interface NavigationDialog : NavigationItem, Parcelable {
    @Composable
    fun Draw(controller: NavigationController)
}
