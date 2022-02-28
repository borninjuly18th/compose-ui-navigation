package ru.bezgrebelnygrigory.composeuinavigation.actions

import ru.bezgrebelnygrigory.composeuinavigation.entity.NavigationItem

interface NavigationAction {
    fun <T : NavigationItem> execute(
        list: List<T>,
        item: T,
    ) : List<T>
}
