package edu.josakapp.proyectoJosakapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.josakapp.proyectoJosakapp.data.di.AppModule
import edu.josakapp.proyectoJosakapp.data.model.User
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val authService = AppModule.authService
    private val userRepository = AppModule.userRepository

    fun register(
        name: String,
        email: String,
        password: String,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        authService.register(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid == null) {
                    onError("No se pudo obtener el UID del usuario")
                    return@addOnSuccessListener
                }

                viewModelScope.launch {
                    try {
                        val user = userRepository.createUser(uid, name, email)
                        onSuccess(user)
                    } catch (e: Exception) {
                        onError(e.message ?: "Error creando usuario")
                    }
                }
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error desconocido")
            }
    }
}
