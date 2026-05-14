package edu.josakapp.proyectoJosakapp.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.ktx.storage
import edu.josakapp.proyectoJosakapp.data.di.AppModule.userRepository
import edu.josakapp.proyectoJosakapp.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class UserViewModel (): ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _seguidoresCount = MutableStateFlow(0)
    val seguidoresCount: StateFlow<Int> = _seguidoresCount

    private val _siguiendoCount = MutableStateFlow(0)
    val siguiendoCount: StateFlow<Int> = _siguiendoCount

    private val _usuariosEncontrados = MutableStateFlow<List<User>>(emptyList())
    val usuariosEncontrados: StateFlow<List<User>> = _usuariosEncontrados


    private var currentUserId: Int? = null

    /** Se llama desde el login cuando ya tenemos el User cargado */
    fun setUser(user: User) {
        _user.value = user
    }

    /** Si quieres actualizar datos del usuario */
    fun updateUser(updated: User) {
        _user.value = updated
    }

    fun refreshUser(userId: Int) {
        viewModelScope.launch {
            val updatedUser = userRepository.getUserById(userId)
            updatedUser?.let { _user.value = it }
        }
    }

    fun loadUserFromId(uid: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            try {
                val loadedUser = userRepository.loadUser(uid)

                if (loadedUser != null) {
                    _user.value = loadedUser
                    onResult(loadedUser)
                } else {
                    onResult(null)
                }
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }
    fun logout() {
        _user.value = null
    }
    fun loadUser(userId: Int) {
        currentUserId = userId
        viewModelScope.launch {
            try {
                val userData = userRepository.getUserById(userId)
                _user.value = userData
            } catch (e: Exception) {
            }
        }
    }

    fun refreshCurrentUser(userId: Int) {
        viewModelScope.launch {
            try {
                val updatedUser = userRepository.getUserById(userId)
                _user.value = updatedUser
                Log.d("UserViewModel", "Usuario refrescado: XP=${updatedUser?.xp_total}")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error refrescando usuario", e)
            }
        }
    }
    fun uploadProfilePicture(context: android.content.Context, uri: android.net.Uri) {
        viewModelScope.launch(Dispatchers.Main) {
            android.widget.Toast.makeText(context, "Procesando imagen...", android.widget.Toast.LENGTH_SHORT).show()
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val originalBitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                if (originalBitmap != null) {
                    val scaledBitmap = android.graphics.Bitmap.createScaledBitmap(originalBitmap, 300, 300, true)

                    val outputStream = java.io.ByteArrayOutputStream()
                    scaledBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 70, outputStream)
                    val bytes = outputStream.toByteArray()

                    val base64Image = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)

                    val currentUser = _user.value
                    if (currentUser != null) {
                        val updatedUser = currentUser.copy(fotoPerfil = base64Image)

                        userRepository.syncUserToRemote(updatedUser)

                        launch(Dispatchers.Main) {
                            _user.value = updatedUser
                            Log.d("FOTO_DEBUG", "¡Éxito! UI Refrescada.")
                        }
                    } else {
                        Log.e("FOTO_DEBUG", "Error: El usuario actual es NULL")
                    }
                }
            } catch (e: Exception) {
                Log.e("FOTO_DEBUG", "ERROR CRÍTICO: ${e.message}")
            }
        }
    }

    /**Función para seguir a alguien*/
    fun followTargetUser(myId: String, targetId: String){
        viewModelScope.launch(Dispatchers.IO){
            try{
                userRepository.followUser(myId, targetId)
                Log.d("SOCIAL_DEBUG", "Usuario $myId ahora sigue) a $targetId")
            } catch (e: Exception){
                Log.e("SOCIAL_DEBUG", "Error al seguir usuario: ${e.message}")
            }
        }
    }
    fun loadSocialStats(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val followers = userRepository.getFollowersCount(userId)
                val following = userRepository.getFollowingCount(userId)

                _seguidoresCount.value = followers
                _siguiendoCount.value = following

                Log.d("SOCIAL_DEBUG", "Stats cargadas: Seguidores=$followers, Siguiendo=$following")
            } catch (e: Exception) {
                Log.e("SOCIAL_DEBUG", "Error cargando stats sociales: ${e.message}")
            }
        }
    }

    fun buscarUsuarios(nombre: String) {
        if (nombre.isBlank()) {
            _usuariosEncontrados.value = emptyList()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = userRepository.searchUsers(nombre)
                Log.d("BUSQUEDA_DEBUG", "1. Firebase encontró: ${result.size} usuarios")

                val listaFiltrada = result.filter { it.uid != _user.value?.uid }
                Log.d("BUSQUEDA_DEBUG", "2. Tras filtrar quedas: ${listaFiltrada.size}")

                _usuariosEncontrados.value = listaFiltrada
            } catch (e: Exception) {
                Log.e("BUSQUEDA_DEBUG", "Error: ${e.message}")
            }
        }
    }

    // Variable para evitar repeticiones mientras la app esté abierta
    private val misionesEntregadasEnSesion = mutableSetOf<String>()

    fun completarMision(userId: Int, tipo: String, xpRecompensa: Int) {
        val currentUser = _user.value ?: return


        if (currentUser.xp_total >= 450) {
            Log.d("XP_DEBUG", "Misión ignorada: El usuario ya tiene suficiente XP ($currentUser.xp_total)")
            return
        }

        // Si es la misión de hábitos (200 XP) y el usuario ya tiene XP,
        // podríamos tener un problema si ya ganó XP por otros medios.
        // Por eso, lo ideal es usar una lista temporal en memoria para la sesión actual

        if (misionesEntregadasEnSesion.contains(tipo)) return

        viewModelScope.launch {
            try {
                val newXp = currentUser.xp_total + xpRecompensa
                val newLevel = User.calculateLevel(newXp)
                val updatedUser = currentUser.copy(xp_total = newXp, nivel = newLevel)

                // Guardar en local y remoto
                userRepository.insertUser(updatedUser)
                userRepository.syncUserToRemote(updatedUser)

                // Actualizar UI
                _user.value = updatedUser
                misionesEntregadasEnSesion.add(tipo) // Marcar para no repetir en esta sesión

                Log.d("XP_DEBUG", "Misión $tipo completada. Nuevo XP: ${updatedUser.xp_total}")
            } catch (e: Exception) {
                Log.e("XP_ERROR", "Error al actualizar XP", e)
            }
        }
    }


    // Añadir monedas, por ejemplo al completar misiones o comprar en la tienda
    fun anyadirMonedas(userId: Int, cantidad: Int) {
        viewModelScope.launch {
            val currentUser = _user.value ?: return@launch

            // Actualizar el número de monedas
            val updatedUser = currentUser.copy(monedas = currentUser.monedas + cantidad)

            //Actualizar en local y remoto
            userRepository.insertUser(updatedUser)
            userRepository.syncUserToRemote(updatedUser)

            // Refrescar UI
            _user.value = updatedUser

            Log.d("MONEDAS", "Añadidas $cantidad monedas. Total: ${updatedUser.monedas}")
        }
    }

    //Actualizar monedas caundo compra en la tienda o reclame recompensas
    fun updateMonedas(cantidad: Int) {
        val userId = _user.value?.id_usuario ?: return
        anyadirMonedas(userId, cantidad)
    }

    // Para el check-in diario, guardamos la última fecha y la racha actual en SharedPreferences
    fun obtenerEstadoCheckIn(context: Context): Pair<Int, Boolean> {
        val prefs = context.getSharedPreferences("checkin_prefs", Context.MODE_PRIVATE)
        val lastDate = prefs.getString("last_date", "") ?: ""
        val nextToClaim = prefs.getInt("next_to_claim", 1) // Recuerda que esto se actualiza cada vez que reclamas, no aquí

        val hoy = java.time.LocalDate.now()
        val hoyStr = hoy.toString()

        // Si es lunes y no se ha reclamado hoy, reseteamos la racha a 1. Si ya se reclamó hoy, mantenemos la racha actual.
        val esLunes = hoy.dayOfWeek == java.time.DayOfWeek.MONDAY
        val yaReclamado = (lastDate == hoyStr)

        // Si es lunes y no se ha reclamado, el progreso es 1. Si ya se reclamó hoy, el progreso es el valor actual de nextToClaim (que se actualiza al reclamar).
        val currentProgress = if (esLunes && !yaReclamado) 1 else nextToClaim

        return Pair(currentProgress, yaReclamado)
    }

    fun ejecutarCheckInManual(context: Context, dayToClaim: Int, userId: Int, cantidad: Int) {
        viewModelScope.launch {
            // Añadir monedas al usuario
            val currentUser = _user.value ?: return@launch
            val updatedUser = currentUser.copy(monedas = currentUser.monedas + cantidad)

            userRepository.insertUser(updatedUser) // Actualizar en local
            userRepository.syncUserToRemote(updatedUser) // Actualizar en remoto
            _user.value = updatedUser // Refrescar UI

            // Actualizar SharedPreferences con la nueva fecha y racha
            val prefs = context.getSharedPreferences("checkin_prefs", Context.MODE_PRIVATE)
            val hoy = java.time.LocalDate.now().toString()
            prefs.edit().apply {
                putString("last_date", hoy)
                putInt("next_to_claim", if (dayToClaim >= 7) 1 else dayToClaim + 1)
                apply()
            }
            Log.d("CHECKIN", "Reclamado día $dayToClaim: $cantidad monedas")
        }
    }

    // Para el regalo diario, guardamos la última fecha
    // y permitimos reclamar solo si ha pasado un día completo
    fun obtenerEstadoRegalo(context: Context): Boolean {
        val prefs = context.getSharedPreferences("gift_prefs", Context.MODE_PRIVATE)
        val lastGiftTime = prefs.getLong("last_gift_timestamp", 0L)
        val currentTime = System.currentTimeMillis()

        // 24 horas en milisegundos = 24 * 60 * 60 * 1000
        return (currentTime - lastGiftTime) >= (24 * 60 * 60 * 1000)
    }

    fun reclamarRegaloAleatorio(context: Context, userId: Int): Int {
        // Porcentaje de probabilidad para cada cantidad de monedas
        val random = java.util.Random()
        val chance = random.nextInt(100) // 0-99

        val cantidad = when {
            chance < 1 -> 1000   // 1% obten 1000
            chance < 5 -> 500    // 4% obten 500
            chance < 15 -> 100   // 10% obten 100
            chance < 40 -> 50    // 25% obten 50
            else -> 10           // 60% obten 10
        }

        // Añadir las monedas al usuario
        anyadirMonedas(userId, cantidad)

        // Actualizar la última fecha de reclamo del regalo
        val prefs = context.getSharedPreferences("gift_prefs", Context.MODE_PRIVATE)
        prefs.edit().putLong("last_gift_timestamp", System.currentTimeMillis()).apply()

        return cantidad
    }



}