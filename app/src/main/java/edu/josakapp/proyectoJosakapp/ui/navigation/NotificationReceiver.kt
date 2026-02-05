package edu.josakapp.proyectoJosakapp.ui.navigation

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import edu.josakapp.proyectoJosakapp.R
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import edu.josakapp.proyectoJosakapp.data.model.HabitoRegistro
import edu.josakapp.proyectoJosakapp.ui.components.HabitoWidget
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "ACTION_YES") {
            val habitoId = intent.getIntExtra("HABITO_ID", 1)

            GlobalScope.launch {
                val db = AppDatabase.getInstance(context)
                val currentHabito = db.habitosDAO().getHabitoById(habitoId)

                if (currentHabito != null) {
                    val nuevoEstado = !currentHabito.estado
                    db.habitosDAO().updateEstado(habitoId, nuevoEstado)

                    if (nuevoEstado) {
                        db.habitosDAO().insertRegistro(HabitoRegistro(habitoId, System.currentTimeMillis()))
                    }

                    val appWidgetManager = AppWidgetManager.getInstance(context)
                    val componentName = ComponentName(context, HabitoWidget::class.java)
                    val views = RemoteViews(context.packageName, R.layout.widget_layout)

                    if (nuevoEstado) {
                        views.setTextViewText(R.id.widget_habito_nombre, " ${currentHabito.nombre}")
                        views.setTextViewText(R.id.btn_widget_cumplir, "Deshacer")
                    } else {
                        views.setTextViewText(R.id.widget_habito_nombre, currentHabito.nombre)
                        views.setTextViewText(R.id.btn_widget_cumplir, "Cumplir")
                    }
                    appWidgetManager.updateAppWidget(componentName, views)
                }
            }
            Toast.makeText(context, "Estado actualizado", Toast.LENGTH_SHORT).show()
        }
    }
}
