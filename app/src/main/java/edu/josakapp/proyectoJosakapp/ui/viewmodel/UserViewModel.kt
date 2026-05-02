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
import com.google.firebase.firestore.FirebaseFirestore
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
        viewModelScope.launch(Dispatchers.IO) {
            val db = FirebaseFirestore.getInstance()
            val snapshot = db.collection("users").get().await()
            val resultados = snapshot.toObjects(User::class.java)
            _usuariosEncontrados.value = resultados
        }
    }
}