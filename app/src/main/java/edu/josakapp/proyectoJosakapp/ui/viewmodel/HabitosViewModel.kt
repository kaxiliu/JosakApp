package edu.josakapp.proyectoJosakapp.ui.viewmodel

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import edu.josakapp.proyectoJosakapp.data.di.AppModule
import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource
import edu.josakapp.proyectoJosakapp.data.model.Habito
import edu.josakapp.proyectoJosakapp.data.model.HabitoRegistro
import edu.josakapp.proyectoJosakapp.data.repository.HabitosRepository
import edu.josakapp.proyectoJosakapp.ui.components.HabitoWidget
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    // Flujo para mantener la racha actual del usuario
    private val _rachaActual = MutableStateFlow(0)
    val rachaActual: StateFlow<Int> = _rachaActual.asStateFlow()

    // Flujo para mantener el total de días activos del usuario
    private val _totalDiasActivos = MutableStateFlow(0)
    val totalDiasActivos: StateFlow<Int> = _totalDiasActivos.asStateFlow()
    private val habitosRepository: HabitosRepository

    // Flujo para notificar cambios en el XP del usuario
    private val _userXpUpdated = MutableSharedFlow<Int>()
    // Exponer el flujo como SharedFlow para que otras partes de la app puedan suscribirse a los cambios de XP
    val userXpUpdated: SharedFlow<Int> = _userXpUpdated.asSharedFlow()

    init {
        val database = AppDatabase.getInstance(application)
        val localDatasource = LocalDatasource(database.usersDAO(), database.habitosDAO(), database.amigosDAO(), database.pinguinoDAO())
        val userRepository = AppModule.userRepository
        habitosRepository = HabitosRepository(localDatasource,userRepository)
        cargarTodosLosRegistros()
    }

    private fun cargarTodosLosRegistros() {
        viewModelScope.launch {
            habitosRepository.getAllRegistros().collect { lista ->
                _todosLosRegistros.value = lista
                // Cada vez que se actualizan los registros,
                // recalculamos las estadísticas
                actualizarEstadisticas()
            }
        }
    }
    private fun actualizarEstadisticas() {
        val registros = _todosLosRegistros.value
        if (registros.isEmpty()) {
            _rachaActual.value = 0
            _totalDiasActivos.value = 0
            return
        }

        // Obtener fechas únicas en formato LocalDate y ordenarlas de más reciente a más antigua
        val fechasUnicas = registros.map {
            java.time.Instant.ofEpochMilli(it.fecha)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }.distinct().sortedDescending()

        // 1. Total de días de actividad
        _totalDiasActivos.value = fechasUnicas.size

        // 2. Cálculo de la racha actual (Streak)
        val hoy = LocalDate.now()
        val ayer = hoy.minusDays(1)

        // Si la última fecha no es hoy ni ayer, la racha se ha roto
        if (fechasUnicas.first() != hoy && fechasUnicas.first() != ayer) {
            _rachaActual.value = 0
        } else {
            var racha = 0
            var fechaEsperada = fechasUnicas.first()
            for (fecha in fechasUnicas) {
                if (fecha == fechaEsperada) {
                    racha++
                    fechaEsperada = fechaEsperada.minusDays(1)
                } else {
                    break
                }
            }
            _rachaActual.value = racha
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
                refreshWidget()
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
                habitosRepository.updateEstado(habito.id_habito, true)
                habitosRepository.addXpToUser(habito.id_usuario, habito.exp_habito)

                // Emitir el ID del usuario para notificar que su XP ha sido actualizado
                _userXpUpdated.emit(habito.id_usuario)
            } else {
                habitosRepository.deleteRegistro(habito.id_habito, hoy)
                habitosRepository.updateEstado(habito.id_habito, false)
            }
            refreshWidget()
        }
    }
    //notificar al Widget que los datos han cambiado y debe actualizarse
    private fun refreshWidget() {
        //Crear un Intent dirigido a nuestra clase HabitoWidget
        val intent = Intent(getApplication(), HabitoWidget::class.java).apply {
            // Establecer la acción estándar de actualización de widgets
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        }
        //Obtener los identificadores
        val ids = AppWidgetManager.getInstance(getApplication())
            .getAppWidgetIds(ComponentName(getApplication(), HabitoWidget::class.java))
        //Pasar los IDs al Intent para que el sistema sepa qué widgets actualizar
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        //Enviar la señal (broadcast) para activar  onUpdate del Widget
        getApplication<Application>().sendBroadcast(intent)
    }

    //Calcular la degradación de la energía de un hábito basado en el
    // tiempo transcurrido desde la última vez que se completó
    fun calcularDegradacionSed(nivelActual: Float, ultimaVez: Long): Float {
        val ahora = System.currentTimeMillis()
        val milisegundosPorDia = 24 * 60 * 60 * 1000L

        // Calcular cuántos días han pasado desde la última vez que se completó el hábito
        val diasPasados = (ahora - ultimaVez).toFloat() / milisegundosPorDia

        // Resta 0.1f por cada día que ha pasado
        val reduccionTotal = diasPasados * 0.1f

        // Calcular el nuevo nivel restando la reducción total al nivel actual
        var nuevoNivel = nivelActual - reduccionTotal

        // Si el nuevo nivel es menor que 0.2f,
        // lo establecemos en 0.2f para evitar que baje demasiado
        if (nuevoNivel < 0.2f) {
            nuevoNivel = 0.2f
        }

        return nuevoNivel
    }
}