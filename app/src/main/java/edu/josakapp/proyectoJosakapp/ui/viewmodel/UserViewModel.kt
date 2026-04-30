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

    private var currentUserId: Int? = null

    /** Se llama desde el login cuando ya tenemos el User cargado */
    fun setUser(user: User) {
        _user.value = user
    }

    /** Si quieres actualizar datos del usuario */
    fun updateUser(updated: User) {
        _user.value = updated
    }

    //Refresca los datos del usuario desde la base de datos
    // útil después de actualizar XP o nivel
    fun refreshUser(userId: Int) {
        viewModelScope.launch {
            val updatedUser = userRepository.getUserById(userId)
            updatedUser?.let { _user.value = it }
        }
    }
    // Dentro de tu UserViewModel.kt

    fun loadUserFromId(uid: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            try {
                // Llamamos al repositorio para obtener los datos del usuario de Firestore/DB
                val loadedUser = userRepository.loadUser(uid)

                if (loadedUser != null) {
                    _user.value = loadedUser // Guardamos el usuario en el State del ViewModel
                    onResult(loadedUser)
                } else {
                    onResult(null)
                }
            } catch (e: Exception) {
                // Si hay un error (ej. no hay internet), devolvemos null
                onResult(null)
            }
        }
    }
    fun logout() {
        _user.value = null // Limpiamos el estado del usuario
    }
    fun loadUser(userId: Int) {
        currentUserId = userId
        viewModelScope.launch {
            try {
                val userData = userRepository.getUserById(userId)
                _user.value = userData
            } catch (e: Exception) {
                // Manejar error
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
                Log.d("FOTO_DEBUG", "Iniciando procesamiento para la URI: $uri")

                val inputStream = context.contentResolver.openInputStream(uri)
                val originalBitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                if (originalBitmap != null) {
                    val scaledBitmap = android.graphics.Bitmap.createScaledBitmap(originalBitmap, 300, 300, true)

                    val outputStream = java.io.ByteArrayOutputStream()
                    scaledBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 70, outputStream)
                    val bytes = outputStream.toByteArray()

                    val base64Image = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
                        .replace("\n", "")
                        .replace("\r", "")

                    val fakeUrl = "data:image/jpeg;base64,$base64Image"

                    // 7. Actualizar en Firebase y en el Estado Local
                    val currentUser = _user.value
                    if (currentUser != null) {
                        val updatedUser = currentUser.copy(fotoPerfil = fakeUrl)

                        userRepository.syncUserToRemote(updatedUser)

                        _user.value = updatedUser
                        launch(Dispatchers.Main) { // Volvemos al hilo principal para tocar la UI
                            _user.value = updatedUser
                            Log.d("FOTO_DEBUG", "¡Éxito! UI Refrescada.")
                        }
                        Log.d("FOTO_DEBUG", "¡Éxito! Imagen convertida y guardada en Firestore.")
                    } else {
                        Log.e("FOTO_DEBUG", "Error: El usuario actual es NULL")
                    }
                } else {
                    Log.e("FOTO_DEBUG", "Error: No se pudo decodificar el Bitmap")
                }
            } catch (e: Exception) {
                Log.e("FOTO_DEBUG", "ERROR CRÍTICO: ${e.message}")
                e.printStackTrace()
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


}