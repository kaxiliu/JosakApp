package edu.josakapp.proyectoJosakapp.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.repository.HabitosRepository


class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HabitosRepository()

    var name by mutableStateOf("")
        private set

    fun updateName(value: String) {
        name = value
    }

    /*
    val productos: List<Habito> by lazy {
        repository.readRawFile()
    }*/
}

