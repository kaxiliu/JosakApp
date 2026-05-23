package edu.josakapp.proyectoJosakapp.data.store

import android.content.Context
import android.util.Log
import edu.josakapp.proyectoJosakapp.data.di.AppModule
import org.json.JSONArray

class StoreRepository {
	private val context: Context = AppModule.getContext()
	private val prefs = context.getSharedPreferences("store_prefs", Context.MODE_PRIVATE)

	fun loadItemsFromAssets(): List<StoreItem> {
		try {
			val json = context.assets.open("tienda.json").bufferedReader().use { it.readText() }
			val array = JSONArray(json)
			val list = mutableListOf<StoreItem>()
			for (i in 0 until array.length()) {
				val obj = array.getJSONObject(i)
				val imageName = if (obj.has("image")) obj.getString("image") else null
				val imageResName = imageName?.substringBeforeLast('.')
				val imageResId = if (!imageResName.isNullOrBlank()) {
					// Primero buscar en paquete de la app
					var id = context.resources.getIdentifier(imageResName, "drawable", context.packageName)
					if (id == 0) {
						// Intentar recurso del framework Android
						id = context.resources.getIdentifier(imageResName, "drawable", "android")
					}
					id
				} else 0

				val item = StoreItem(
					id = obj.optString("id", "item_$i"),
					name = obj.optString("name", "Item $i"),
					description = obj.optString("description", ""),
					price = obj.optInt("price", 0),
					rarity = obj.optString("rarity", "common"),
					category = obj.optString("category", "general"),
					imageResName = imageResName,
					imageResId = imageResId
				)
				list.add(item)
			}
			return list
		} catch (e: Exception) {
			Log.e("StoreRepository", "Error leyendo tienda.json: ${e.message}")
			return emptyList()
		}
	}

	fun getOwnedIds(): Set<String> = prefs.getStringSet("store_owned_items", emptySet()) ?: emptySet()

	fun markOwned(id: String) {
		val set = getOwnedIds().toMutableSet()
		set.add(id)
		prefs.edit().putStringSet("store_owned_items", set).apply()
	}
}


