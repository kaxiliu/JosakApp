package edu.josakapp.proyectoJosakapp.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.model.HabitoRegistro
import edu.josakapp.proyectoJosakapp.data.repository.HabitosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

class HabitosViewModel(application: Application) : AndroidViewModel(application) {

    private val _habitos = MutableStateFlow<List<Habito>>(emptyList())
    val habitos: StateFlow<List<Habito>> = _habitos.asStateFlow()

    private val _registrosHoy = MutableStateFlow<List<HabitoRegistro>>(emptyList())
    private val _todosLosRegistros = MutableStateFlow<List<HabitoRegistro>>(emptyList())
    val todosLosRegistros: StateFlow<List<HabitoRegistro>> = _todosLosRegistros.asStateFlow()
    private val habitosRepository: HabitosRepository

    init {
        val database = AppDatabase.getInstance(application)
        val localDatasource = LocalDatasource(database.usersDAO(), database.habitosDAO(), database.amigosDAO())
        habitosRepository = HabitosRepository(localDatasource)
        cargarTodosLosRegistros()
    }

    private fun cargarTodosLosRegistros() {
        viewModelScope.launch {
            habitosRepository.getAllRegistros().collect { lista ->
                _todosLosRegistros.value = lista
            }
        }
    }

    /**Cargar hábitos de un usuario específico*/
    fun loadHabitos(userId: Int) {
        viewModelScope.launch {
            habitosRepository.getHabitosByUserId(userId)
                .catch { e ->
                    Log.e("HabitosViewModel", "Error loading habitos: ${e.message}", e)
                }
                .collect { list ->
                    _habitos.value = list
                }
        }
    }

    /**Guardar un nuevo hábito*/
    fun saveHabito(habito: Habito) {
        viewModelScope.launch {
            try {
                habitosRepository.insertHabito(habito)
                Log.d("HabitosViewModel", "Habito saved successfully")
            } catch (e: Exception) {
                Log.e("HabitosViewModel", "Error saving habito: ${e.message}", e)
            }
        }
    }

    /**Actualizar el estado de un hábito*/
    fun updateEstado(id: Int, estado: Boolean) {
        viewModelScope.launch {
            try {
                habitosRepository.updateEstado(id, estado)
                Log.d("HabitosViewModel", "Habito estado updated successfully")
            } catch (e: Exception) {
                Log.e("HabitosViewModel", "Error updating habito estado: ${e.message}", e)
            }
        }
    }

    /**Actualizar un hábito completo*/
    fun updateHabito(habito: Habito) {
        viewModelScope.launch {
            try {
                habitosRepository.updateHabito(habito)
                Log.d("HabitosViewModel", "Habito updated successfully")
            } catch (e: Exception) {
                Log.e("HabitosViewModel", "Error updating habito: ${e.message}", e)
            }
        }
    }

    /**Eliminar un hábito*/
    fun deleteHabito(habito: Habito) {
        viewModelScope.launch {
            try {
                habitosRepository.deleteHabito(habito)
                Log.d("HabitosViewModel", "Habito deleted successfully")
            } catch (e: Exception) {
                Log.e("HabitosViewModel", "Error deleting habito: ${e.message}", e)
            }
        }
    }

    fun toggleHabito(habito: Habito) {
        val hoy = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val estaCompletadoHoy = _todosLosRegistros.value.any {
            it.id_habito == habito.id_habito && it.fecha == hoy
        }

        viewModelScope.launch {
            if (!estaCompletadoHoy) {
                habitosRepository.insertRegistro(HabitoRegistro(habito.id_habito, hoy))
                //updateEstado(habito.id_habito, true)
            } else {
                habitosRepository.deleteRegistro(habito.id_habito, hoy)
                //updateEstado(habito.id_habito, false)
            }
        }
    }
}