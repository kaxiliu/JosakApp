package edu.josakapp.proyectoJosakapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.josakapp.proyectoJosakapp.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    /** Se llama desde el login cuando ya tenemos el User cargado */
    fun setUser(user: User) {
        _user.value = user
    }

    /** Si quieres actualizar datos del usuario */
    fun updateUser(updated: User) {
        _user.value = updated
    }
}
