package edu.josakapp.proyectoJosakapp.data.store

data class StoreItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val rarity: String = "common",
    val category: String = "general",
    val imageResName: String? = null,
    val imageResId: Int = 0
)

