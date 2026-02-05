package edu.josakapp.proyectoJosakapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.josakapp.proyectoJosakapp.data.di.AppModule
import edu.josakapp.proyectoJosakapp.data.model.User
import edu.josakapp.proyectoJosakapp.data.network.AuthService
import edu.josakapp.proyectoJosakapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    // Creamos aquí las dependencias
    private val authService = AuthService()
    private val userRepository = AppModule.userRepository

    fun onNameChange(newName: String) {
        uiState = uiState.copy(name = newName)
    }

    fun onPassChange(newPass: String) {
        uiState = uiState.copy(pass = newPass)
    }

    fun login(
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        val email = uiState.name.trim()
        val pass = uiState.pass.trim()

        if (email.isEmpty() || pass.isEmpty()) {
            onError("Los campos no pueden estar vacíos")
            return
        }

        authService.login(email, pass)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid == null) {
                    onError("No se pudo obtener el UID del usuario")
                    return@addOnSuccessListener
                }

                viewModelScope.launch {
                    try {
                        val user = userRepository.loadUser(uid)
                        onSuccess(user)
                    } catch (e: Exception) {
                        onError(e.message ?: "Error cargando usuario")
                    }
                }
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error desconocido")
            }
    }
}

data class LoginUiState(
    val name: String = "",
    val pass: String = ""
)
