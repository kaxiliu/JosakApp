package edu.josakapp.proyectoJosakapp.ui.navigation

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.Toast
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import edu.josakapp.proyectoJosakapp.data.model.HabitoRegistro
import edu.josakapp.proyectoJosakapp.data.util.saveHabitLastCompletedAt
import edu.josakapp.proyectoJosakapp.ui.components.HabitoWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationReceiver : BroadcastReceiver() {
    // Se ejecuta cuando el usuario hace clic en el botón del Widget
    override fun onReceive(context: Context, intent: Intent) {
        // Verifica si la acción es la correcta para actualizar el hábito
        if (intent.action == "ACTION_YES") {
            // Obtiene el ID del hábito
            val habitoId = intent.getIntExtra("HABITO_ID", 1)
            if (habitoId == -1) return

            // Inicia una corrutina para realizar operaciones de base de datos en segundo plano
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getInstance(context)
                val currentHabito = db.habitosDAO().getHabitoById(habitoId)
                val hoy = java.time.LocalDate.now() // Calcula el tiempo de hoy
                    .atStartOfDay(java.time.ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()


                if (currentHabito != null) {
                    val nuevoEstado = !currentHabito.estado // Cambia el estado
                    db.habitosDAO().updateEstado(habitoId, nuevoEstado)
                    // Si ahora está completado, inserta un registro; si no, lo elimina
                    if (nuevoEstado) {
                        db.habitosDAO().insertRegistro(HabitoRegistro(habitoId, hoy))
                        saveHabitLastCompletedAt(context, habitoId, System.currentTimeMillis())
                    }else {
                        db.habitosDAO().deleteRegistro(habitoId, hoy)
                    }

                    // Actualización inmediata del Widget
                    val appWidgetManager = AppWidgetManager.getInstance(context)
                    val componentName = ComponentName(context, HabitoWidget::class.java)
                    val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
                    // Llama  updateWidget para refrescar la interfaz visual
                    for (id in appWidgetIds) {
                        HabitoWidget.updateWidget(context, appWidgetManager, id)
                    }
                }
            }
            Toast.makeText(context, "Estado actualizado", Toast.LENGTH_SHORT).show()
        }
    }
}
