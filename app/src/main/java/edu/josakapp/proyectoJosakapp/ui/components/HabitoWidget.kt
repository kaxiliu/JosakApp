package edu.josakapp.proyectoJosakapp.ui.components

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import edu.josakapp.proyectoJosakapp.MainActivity
import edu.josakapp.proyectoJosakapp.R
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import edu.josakapp.proyectoJosakapp.ui.navigation.NotificationReceiver
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HabitoWidget : AppWidgetProvider() {

    // Se llama cuando el sistema solicita actualizar el widget
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }
    companion object {
        // Actualizar la interfaz del widget
        fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            //Configurar el clic para abrir la App
            val appIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val appPendingIntent = PendingIntent.getActivity(
                context,
                0,
                appIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            // Al tocar el fondo del widget ,se abre la App
            views.setOnClickPendingIntent(R.id.widget_root, appPendingIntent)

            GlobalScope.launch {    // Lógica asíncrona para la base de datos ---
                val db = AppDatabase.getInstance(context)
                val habitsList = db.habitosDAO().getAllHabitos().firstOrNull()
                val firstHabito = habitsList?.firstOrNull()

                val hoy = java.time.LocalDate.now() // Obtener el tiempo de hoy a las 00:00:00
                    .atStartOfDay(java.time.ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()


                if (firstHabito != null) {
                    // Verificar si el hábito ya fue completado hoy
                    val registros = db.habitosDAO().getAllRegistros().firstOrNull() ?: emptyList()
                    val isCompletadoHoy = registros.any { it.id_habito == firstHabito.id_habito && it.fecha == hoy }
                    // Actualizar el nombre y el icono según el estado
                    views.setTextViewText(R.id.widget_habito_nombre, firstHabito.nombre)
                    val iconRes = if (isCompletadoHoy) R.drawable.check else R.drawable.unchecked
                    views.setImageViewResource(R.id.btn_widget_cumplir, iconRes)
                    //Configurar el clic del botón de completar
                    val intent = Intent(context, NotificationReceiver::class.java).apply {
                        action = "ACTION_YES"
                        putExtra("HABITO_ID", firstHabito.id_habito)
                    }
                    val pendingIntent = PendingIntent.getBroadcast(
                        context, firstHabito.id_habito, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    views.setOnClickPendingIntent(R.id.btn_widget_cumplir, pendingIntent)
                } else {
                    views.setTextViewText(R.id.widget_habito_nombre, "No hay hábitos")
                }
                // Aplicar todos los cambios al widget
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }

}

/*
    private fun showWaterNotification(context: Context) {
        val channelId = "HABIT_CHANNEL"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "Hábitos", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val yesIntent = Intent(context, NotificationReceiver::class.java).apply { action = "ACTION_YES" }
        val yesPendingIntent = PendingIntent.getBroadcast(
            context, 0, yesIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.fire)
            .setContentTitle("Recordatorio")
            .setContentText("¿Has bebido agua? Sí/No")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.logo, "Sí", yesPendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(1, builder.build())
    }
 */


