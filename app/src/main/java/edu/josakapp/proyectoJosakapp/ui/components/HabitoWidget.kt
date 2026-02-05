package edu.josakapp.proyectoJosakapp.ui.components

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import edu.josakapp.proyectoJosakapp.R
import edu.josakapp.proyectoJosakapp.data.datasource.AppDatabase
import edu.josakapp.proyectoJosakapp.ui.navigation.NotificationReceiver
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HabitoWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)

            GlobalScope.launch {
                val db = AppDatabase.getInstance(context)
                val habitsList = db.habitosDAO().getAllHabitos().firstOrNull()
                val firstHabito = habitsList?.firstOrNull()

                if (firstHabito != null) {
                    if (firstHabito.estado) {
                        views.setTextViewText(R.id.widget_habito_nombre, "${firstHabito.nombre}")
                        views.setTextViewText(R.id.btn_widget_cumplir, "Deshacer")
                    } else {
                        views.setTextViewText(R.id.widget_habito_nombre, firstHabito.nombre)
                        views.setTextViewText(R.id.btn_widget_cumplir, "Cumplir")
                    }

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


