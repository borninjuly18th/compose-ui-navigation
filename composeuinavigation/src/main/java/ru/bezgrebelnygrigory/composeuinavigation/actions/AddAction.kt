package ru.bezgrebelnygrigory.composeuinavigation.actions

import ru.bezgrebelnygrigory.composeuinavigation.entity.NavigationItem

class AddAction : NavigationAction {
    override  fun <T : NavigationItem> execute(
        list: List<T>,
        item: T,
    ) : List<T> = list + item
}
