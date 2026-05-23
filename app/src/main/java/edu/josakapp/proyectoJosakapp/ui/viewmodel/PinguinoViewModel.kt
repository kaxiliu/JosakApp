package edu.josakapp.proyectoJosakapp.ui.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.josakapp.proyectoJosakapp.data.di.AppModule
import edu.josakapp.proyectoJosakapp.data.model.Accesorios
import edu.josakapp.proyectoJosakapp.data.model.Pinguino
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Context
import org.json.JSONObject

class PinguinoViewModel(application: Application) : AndroidViewModel(application) {

    private val pinguinoRepository = AppModule.pinguinoRepository
    private val userRepository = AppModule.userRepository

    private val _nivelSed = MutableStateFlow(1.0f)// El nivel de sed del pingüino
    val nivelSed = _nivelSed.asStateFlow()

    private val _mochila = MutableStateFlow<Map<Int, Int>>(emptyMap())// La mochila del pingüino
    val mochila = _mochila.asStateFlow()

    val tiendaDeRopa = listOf(
        Accesorios(id_accesorio = 1, nombre = "Pirata", descripcion = "Sombrero de pirata", imagen = "pirata", precio = 100f, tipo = true, id_pinguino = 0),
        Accesorios(id_accesorio = 2, nombre = "Árbol", descripcion = "Sombrero de árbol", imagen = "arbol", precio = 30f, tipo = true, id_pinguino = 0),
        Accesorios(id_accesorio = 3, nombre = "Magia", descripcion = "Sombrero mágico", imagen = "magia", precio = 200f, tipo = true, id_pinguino = 0),
        Accesorios(id_accesorio = 4, nombre = "Mushroom", descripcion = "Sombrero de hongo", imagen = "mushroom", precio = 40f, tipo = true, id_pinguino = 0),
        Accesorios(id_accesorio = 5, nombre = "Fresa", descripcion = "Sombrero de fresa", imagen = "fresa", precio = 30f, tipo = true, id_pinguino = 0),
        Accesorios(id_accesorio = 6, nombre = "Calabaza", descripcion = "Sombrero de calabaza", imagen = "calabaza", precio = 60f, tipo = true, id_pinguino = 0),
        Accesorios(id_accesorio = 7, nombre = "Flores", descripcion = "Sombrero de flores", imagen = "flores", precio = 60f, tipo = true, id_pinguino = 0),
        Accesorios(id_accesorio = 8, nombre = "Navidad", descripcion = "Sombrero navideño", imagen = "navidad", precio = 50f, tipo = true, id_pinguino = 0)
    )

    private val _mochilaRopa = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val mochilaRopa = _mochilaRopa.asStateFlow()

    private val _ropaEquipadaRes = MutableStateFlow<Int?>(null)
    val ropaEquipadaRes = _ropaEquipadaRes.asStateFlow()

    fun cargarEquipada(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val key = "equipped_user_$userId"
                val value = prefs.getInt(key, -1)
                withContext(Dispatchers.Main) {
                    _ropaEquipadaRes.value = if (value >= 0) value else null
                }
            } catch (e: Exception) {
                // ignore
            }
        }
    }

    private fun saveEquipadaForUser(userId: Int, resId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val key = "equipped_user_$userId"
                if (resId == null) prefs.edit().remove(key).apply()
                else prefs.edit().putInt(key, resId).apply()
            } catch (e: Exception) {
                // ignore
            }
        }
    }

    fun cargarRopaComprada(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val pinguinoWithAccesorios = pinguinoRepository.getRopaComprada(userId)
            val mapComprados = mutableMapOf<String, Boolean>()

            pinguinoWithAccesorios?.accesorios?.forEach { accesorio ->
                mapComprados[accesorio.imagen] = true
            }

            withContext(Dispatchers.Main) {
                _mochilaRopa.value = mapComprados
            }
        }
    }

    // Persistencia simple de la mochila por usuario usando SharedPreferences
    private val prefs by lazy {
        AppModule.getContext().getSharedPreferences("pinguino_prefs", Context.MODE_PRIVATE)
    }

    fun cargarMochila(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val key = "mochila_user_$userId"
            val json = prefs.getString(key, null)
            val map = mutableMapOf<Int, Int>()
            if (!json.isNullOrBlank()) {
                try {
                    val obj = JSONObject(json)
                    val keys = obj.keys()
                    while (keys.hasNext()) {
                        val k = keys.next()
                        val value = obj.optInt(k, 0)
                        // keys were saved as strings of resId
                        map[k.toIntOrNull() ?: continue] = value
                    }
                } catch (e: Exception) {
                    // ignore and keep empty
                }
            }

            withContext(Dispatchers.Main) {
                _mochila.value = map
            }
        }
    }

    private fun saveMochilaForUser(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val key = "mochila_user_$userId"
                val json = JSONObject()
                _mochila.value.forEach { (k, v) -> json.put(k.toString(), v) }
                prefs.edit().putString(key, json.toString()).apply()
            } catch (e: Exception) {
                // ignore
            }
        }
    }

    fun comprarRopa(resId: Int, accesorio: Accesorios, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            var pinguino = pinguinoRepository.getPinguino(userId)

            if (pinguino == null) {
                val nuevoPinguino = Pinguino(
                    nivel = 1, xp_actual = 0, estado = true,
                    nombre = "Pingüino $userId", id_usuario = userId,
                    nivelSed = 1.0f, ultimaVezSed = System.currentTimeMillis()
                )
                pinguinoRepository.guardarPinguino(nuevoPinguino)
                pinguino = pinguinoRepository.getPinguino(userId)
            }

            pinguino?.let {
                pinguinoRepository.registrarCompraRopa(it.idPinguino, accesorio.id_accesorio)

                withContext(Dispatchers.Main) {
                    _mochilaRopa.value = _mochilaRopa.value + (accesorio.imagen to true)
                }
            }
        }
    }

    // Función para inicializar el estado del pingüino al cargar la app,
    // calculando la pérdida de sed en función del tiempo transcurrido desde la última sesión
    fun inicializarEstadoPinguino(nivelGuardado: Float, ultimaFecha: Long) {
        val ahora = System.currentTimeMillis()
        val milisegundosPorDia = 24 * 60 * 60 * 1000L
        val diasPasados = (ahora - ultimaFecha).toFloat() / milisegundosPorDia

        val reduccion = diasPasados * 0.1f

        val nuevoNivel = (nivelGuardado - reduccion).coerceIn(0.2f, 1.0f)
        _nivelSed.value = nuevoNivel
    }

    // Función para comprar bebidas y actualizar la mochila del pingüino
    fun comprarBebida(resId: Int, cantidad: Int, userId: Int) {
        val actual = _mochila.value[resId] ?: 0
        _mochila.value = _mochila.value + (resId to (actual + cantidad))
        // Guardar persistente para el usuario
        if (userId != 0) saveMochilaForUser(userId)
    }

    // Función para usar una bebida de la mochila y reducir el nivel de sed del pingüino
    fun usarBebida(resId: Int, cantidad: Int, userId: Int) {
        val actual = _mochila.value[resId] ?: 0
        if (actual >= cantidad) {
            val nuevaCantidad = actual - cantidad

            if (nuevaCantidad > 0) {
                // Si aún quedan bebidas después de usar, actualizamos la cantidad en la mochila
                _mochila.value = _mochila.value + (resId to nuevaCantidad)
            } else {
                // Si se han usado todas las bebidas, eliminamos la entrada de la mochila
                _mochila.value = _mochila.value - resId
            }
            darDeBeber(cantidad, userId)
            if (userId != 0) saveMochilaForUser(userId)
        }
    }
    

    // Función para aumentar el nivel de sed del pingüino al usar una bebida,
    // con un incremento de 0.1 por cada bebida consumida
    fun darDeBeber(cantidad: Int, userId: Int) {
        val nuevoValor = _nivelSed.value + (0.1f * cantidad)
        _nivelSed.value = nuevoValor.coerceAtMost(1.0f)

        viewModelScope.launch(Dispatchers.IO) {
            val pinguinoActual = pinguinoRepository.getPinguino(userId)

            pinguinoActual?.let {
                val pinguinoActualizado = it.copy(
                    nivelSed = nuevoValor, // Guardar el nuevo nivel de sed después de beber
                    ultimaVezSed = System.currentTimeMillis() // Actualizar la última vez que se le dio de beber al pingüino
                )
                pinguinoRepository.guardarPinguino(pinguinoActualizado)
            }
        }
    }

    fun equiparRopa(resId: Int, userId: Int = 0) {
        viewModelScope.launch(Dispatchers.Main) {
            if (_ropaEquipadaRes.value == resId) {
                _ropaEquipadaRes.value = null
                if (userId != 0) saveEquipadaForUser(userId, null)
            } else {
                _ropaEquipadaRes.value = resId
                if (userId != 0) saveEquipadaForUser(userId, resId)
            }
        }
    }
}