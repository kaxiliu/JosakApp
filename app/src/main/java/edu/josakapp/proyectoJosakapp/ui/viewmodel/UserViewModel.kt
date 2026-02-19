package edu.josakapp.proyectoJosakapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.josakapp.proyectoJosakapp.data.di.AppModule.userRepository
import edu.josakapp.proyectoJosakapp.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
}