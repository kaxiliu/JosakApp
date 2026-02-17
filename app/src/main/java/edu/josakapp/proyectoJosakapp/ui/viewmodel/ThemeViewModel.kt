package edu.josakapp.proyectoJosakapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


/**View model que se encargará de cambiar el color del tema*/
class ThemeViewModel :  ViewModel() {
    var isDarkMode by mutableStateOf(false)
        private set
    fun toggleTheme() {
        isDarkMode = !isDarkMode
    }
}