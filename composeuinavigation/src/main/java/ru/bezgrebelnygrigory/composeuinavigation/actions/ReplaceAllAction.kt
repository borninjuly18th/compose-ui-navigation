package ru.bezgrebelnygrigory.composeuinavigation.actions

import ru.bezgrebelnygrigory.composeuinavigation.entity.NavigationItem

/** Удаление текущих экранов и view model */
class ReplaceAllAction : NavigationAction {
    override fun <T : NavigationItem> execute(
        list: List<T>,
        item: T,
    ) : List<T> = listOf(item)
}
