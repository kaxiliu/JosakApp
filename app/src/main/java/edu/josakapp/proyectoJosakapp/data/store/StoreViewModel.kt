package edu.josakapp.proyectoJosakapp.data.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.ZoneOffset
import java.time.ZonedDateTime

data class StoreItemUi(val item: StoreItem, val isOwned: Boolean)

class StoreViewModel : ViewModel() {
    private val repo = StoreRepository()

    private val _items = MutableStateFlow<List<StoreItemUi>>(emptyList())
    val items: StateFlow<List<StoreItemUi>> = _items

    // Artículos rotativos del día (4 items)
    private val _dailyItems = MutableStateFlow<List<StoreItem>>(emptyList())
    val dailyItems: StateFlow<List<StoreItem>> = _dailyItems

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            val loaded = repo.loadItemsFromAssets()
            val owned = repo.getOwnedIds()
            _items.value = loaded.map { StoreItemUi(it, owned.contains(it.id)) }
            // actualizar daily items
            _dailyItems.value = computeDailyRotation(loaded, 4)
        }
    }

    fun markOwned(id: String) {
        repo.markOwned(id)
        refresh()
    }

    private fun computeDailyRotation(allItems: List<StoreItem>, count: Int): List<StoreItem> {
        if (allItems.isEmpty()) return emptyList()
        // Determinar día en GMT+2
        val zone = ZoneOffset.ofHours(2)
        val now = ZonedDateTime.now(zone)
        // Alternar por día: par/impar
        val dayIndex = now.dayOfYear % 2 // 0 o 1
        val start = (dayIndex * count) % allItems.size
        val result = mutableListOf<StoreItem>()
        var idx = start
        while (result.size < count) {
            result.add(allItems[idx % allItems.size])
            idx++
        }
        return result
    }
}

